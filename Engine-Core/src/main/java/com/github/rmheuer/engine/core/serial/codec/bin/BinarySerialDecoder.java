package com.github.rmheuer.engine.core.serial.codec.bin;

import com.github.rmheuer.engine.core.serial.node.SerialArray;
import com.github.rmheuer.engine.core.serial.node.SerialBoolean;
import com.github.rmheuer.engine.core.serial.node.SerialByte;
import com.github.rmheuer.engine.core.serial.node.SerialChar;
import com.github.rmheuer.engine.core.serial.node.SerialDouble;
import com.github.rmheuer.engine.core.serial.node.SerialFloat;
import com.github.rmheuer.engine.core.serial.node.SerialInt;
import com.github.rmheuer.engine.core.serial.node.SerialLong;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.node.SerialReference;
import com.github.rmheuer.engine.core.serial.node.SerialShort;
import com.github.rmheuer.engine.core.serial.node.SerialString;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.rmheuer.engine.core.serial.codec.bin.BinaryTypeIds.*;

public final class BinarySerialDecoder {
    private final DataInputStream in;
    private final Map<Integer, List<SerialReference>> unsatisfiedReferences;
    private final Map<Integer, SerialNode> knownReferences;

    public BinarySerialDecoder(DataInputStream in) {
	this.in = in;
	unsatisfiedReferences = new HashMap<>();
	knownReferences = new HashMap<>();
    }

    private int readVarInt() throws IOException {
	int value = 0;
	int length = 0;
	byte currentByte;

	do {
	    currentByte = in.readByte();
	    value |= (currentByte & 0x7F) << (length * 7);

	    length++;
	    if (length > 5) {
		throw new IOException("VarInt too big");
	    }
	} while ((currentByte & 0x80) == 0x80);

	return value;
    }

    private long readVarLong() throws IOException {
	long value = 0;
	int length = 0;
	byte currentByte;

        do {
	    currentByte = in.readByte();
	    value |= (long) (currentByte & 0x7F) << (length * 7);

	    length++;
	    if (length > 10) {
		throw new IOException("VarLong too big");
	    }
	} while ((currentByte & 0x80) == 0x80);

	return value;
    }

    private SerialByte decodeByte() throws IOException {
	return new SerialByte(in.readByte());
    }

    private SerialShort decodeShort() throws IOException {
	return new SerialShort(in.readShort());
    }

    private SerialInt decodeInt() throws IOException {
	return new SerialInt(readVarInt());
    }

    private SerialLong decodeLong() throws IOException {
	return new SerialLong(readVarLong());
    }

    private SerialFloat decodeFloat() throws IOException {
	return new SerialFloat(in.readFloat());
    }

    private SerialDouble decodeDouble() throws IOException {
	return new SerialDouble(in.readDouble());
    }

    private SerialBoolean decodeBoolean() throws IOException {
	return new SerialBoolean(in.readBoolean());
    }

    private SerialChar decodeChar() throws IOException {
	return new SerialChar(in.readChar());
    }

    private SerialString decodeString() throws IOException {
	return new SerialString(in.readUTF());
    }

    private SerialObject decodeObject() throws IOException {
	SerialObject obj = new SerialObject();
	
	int entryCount = readVarInt();
	for (int i = 0; i < entryCount; i++) {
	    String key = in.readUTF();
	    SerialNode value = decodeNode();
	    obj.put(key, value);
	}

	return obj;
    }

    private SerialArray decodeArray() throws IOException {
	SerialArray arr = new SerialArray();

	int length = readVarInt();
	for (int i = 0; i < length; i++) {
	    arr.add(decodeNode());
	}

	return arr;
    }

    private SerialReference decodeReference() throws IOException {
	int refId = readVarInt();

	SerialReference ref;
	if (knownReferences.containsKey(refId)) {
	    ref = new SerialReference(knownReferences.get(refId));
	} else {
	    ref = new SerialReference(null);
	    List<SerialReference> refs = unsatisfiedReferences.computeIfAbsent(refId, (id) -> new ArrayList<>());
	    refs.add(ref);
	}

	return ref;
    }

    private SerialNode decodeNode() throws IOException {
	int refId = readVarInt();

	byte typeId = in.readByte();
	SerialNode node;
	switch (typeId) {
	    case ID_BYTE:      node = decodeByte();      break;
	    case ID_SHORT:     node = decodeShort();     break;
	    case ID_INT:       node = decodeInt();       break;
	    case ID_LONG:      node = decodeLong();      break;
	    case ID_FLOAT:     node = decodeFloat();     break;
	    case ID_DOUBLE:    node = decodeDouble();    break;
	    case ID_BOOLEAN:   node = decodeBoolean();   break;
	    case ID_CHAR:      node = decodeChar();      break;
	    case ID_STRING:    node = decodeString();    break;
	    case ID_OBJECT:    node = decodeObject();    break;
	    case ID_ARRAY:     node = decodeArray();     break;
	    case ID_REFERENCE: node = decodeReference(); break;
	    case ID_NULL:      node = null;              break;
	    default:
	        throw new IOException("Unknown type id " + typeId);
	}

	if (refId != 0) {
	    if (unsatisfiedReferences.containsKey(refId)) {
		List<SerialReference> refs = unsatisfiedReferences.remove(refId);
		for (SerialReference ref : refs) {
		    ref.setReference(node);
		}
	    }

	    if (knownReferences.containsKey(refId)) {
		throw new IOException("Duplicate reference id " + refId);
	    }
	    knownReferences.put(refId, node);
	}

	return node;
    }

    public SerialNode decode() throws IOException {
	SerialNode node = decodeNode();
	in.close();
	if (!unsatisfiedReferences.isEmpty()) {
	    StringBuilder message = new StringBuilder("Unsatisfied reference ids: ");
	    boolean comma = false;
	    for (int key : unsatisfiedReferences.keySet()) {
		if (comma) message.append(", ");
		else comma = true;

		message.append(key);
	    }
	    throw new IOException(message.toString());
	}
	return node;
    }
}
