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
import com.github.rmheuer.engine.core.util.CheckedTypeSwitch;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

import static com.github.rmheuer.engine.core.serial.codec.bin.BinaryTypeIds.*;

public final class BinarySerialEncoder {
    private final DataOutputStream out;
    private final Map<SerialNode, Integer> referenceIds;

    public BinarySerialEncoder(DataOutputStream out) {
	this.out = out;
	referenceIds = new IdentityHashMap<>();
    }

    private int preprocess(SerialNode node, int nextRefId) {
	if (node instanceof SerialReference) {
	    SerialReference ref = (SerialReference) node;
	    referenceIds.put(ref.getReference(), nextRefId++);
	} else if (node != null) {
	    for (SerialNode child : node.getChildren()) {
		nextRefId = preprocess(child, nextRefId);
	    }
	}
	return nextRefId;
    }

    // Primarily to save space with reference IDs
    private void writeVarInt(int value) throws IOException {
	while (true) {
	    if ((value & ~0x7F) == 0) {
		out.writeByte(value);
		return;
	    }

	    out.writeByte((value & 0x7F) | 0x80);
	    value >>>= 7;
	}
    }

    private void writeVarLong(long value) throws IOException {
	while (true) {
	    if ((value & ~0x7F) == 0) {
		out.writeByte((byte) value);
		return;
	    }

	    out.writeByte((byte) ((value & 0x7F) | 0x80));
	    value >>>= 7;
	}
    }

    private void encodeByte(SerialByte b) throws IOException {
	out.writeByte(ID_BYTE);
	out.writeByte(b.getValue());
    }

    private void encodeShort(SerialShort s) throws IOException {
	out.writeByte(ID_SHORT);
	out.writeShort(s.getValue());
    }

    private void encodeInt(SerialInt i) throws IOException {
	out.writeByte(ID_INT);
	writeVarInt(i.getValue());
    }

    private void encodeLong(SerialLong l) throws IOException {
	out.writeByte(ID_LONG);
	writeVarLong(l.getValue());
    }

    private void encodeFloat(SerialFloat f) throws IOException {
	out.writeByte(ID_FLOAT);
	out.writeFloat(f.getValue());
    }

    private void encodeDouble(SerialDouble d) throws IOException {
	out.writeByte(ID_DOUBLE);
	out.writeDouble(d.getValue());
    }

    private void encodeBoolean(SerialBoolean b) throws IOException {
	out.writeByte(ID_BOOLEAN);
	out.writeBoolean(b.getValue());
    }

    private void encodeChar(SerialChar c) throws IOException {
	out.writeByte(ID_CHAR);
	out.writeChar(c.getValue());
    }

    private void encodeString(SerialString s) throws IOException {
	out.writeByte(ID_STRING);
	out.writeUTF(s.getValue());
    }

    private void encodeObject(SerialObject o) throws IOException {
	out.writeByte(ID_OBJECT);
	writeVarInt(o.size());
	for (String key : o.keySet()) {
	    out.writeUTF(key);
	    encodeNode(o.getRaw(key));
	}
    }

    private void encodeArray(SerialArray a) throws IOException {
	out.writeByte(ID_ARRAY);
	writeVarInt(a.size());
	for (int i = 0; i < a.size(); i++) {
	    encodeNode(a.getRaw(i));
	}
    }

    private void encodeReference(SerialReference r) throws IOException {
	out.writeByte(ID_REFERENCE);
	SerialNode ref = r.getReference();
	if (!referenceIds.containsKey(ref)) {
	    throw new IOException("Reference target not within context");
	}
	writeVarInt(referenceIds.get(ref));
    }

    private void encodeNull() throws IOException {
	out.writeByte(ID_NULL);
    }

    private void encodeNode(SerialNode node) throws IOException {
	writeVarInt(referenceIds.getOrDefault(node, 0));

	if (node == null) {
	    encodeNull();
	} else {
	    new CheckedTypeSwitch<>(node)
		.doCase(SerialByte.class,      this::encodeByte)
		.doCase(SerialShort.class,     this::encodeShort)
		.doCase(SerialInt.class,       this::encodeInt)
		.doCase(SerialLong.class,      this::encodeLong)
		.doCase(SerialFloat.class,     this::encodeFloat)
		.doCase(SerialDouble.class,    this::encodeDouble)
		.doCase(SerialBoolean.class,   this::encodeBoolean)
		.doCase(SerialChar.class,      this::encodeChar)
		.doCase(SerialString.class,    this::encodeString)
		.doCase(SerialObject.class,    this::encodeObject)
		.doCase(SerialArray.class,     this::encodeArray)
		.doCase(SerialReference.class, this::encodeReference);
	}
    }
    
    public void encode(SerialNode node) throws IOException {
	preprocess(node, 1);
	encodeNode(node);
    }

    public void close() throws IOException {
    	out.close();
	}
}
