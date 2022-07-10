package com.github.rmheuer.engine.core.serial.codec.bin;

import com.github.rmheuer.engine.core.serial.codec.SerialCodec;
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
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class BinarySerialCodec implements SerialCodec {
    private static final BinarySerialCodec INSTANCE = new BinarySerialCodec();

    public static BinarySerialCodec get() {
	return INSTANCE;
    }

    private BinarySerialCodec() {}
    
    @Override
    public void encode(SerialNode node, OutputStream rawOut) throws IOException {
	DataOutputStream out = new DataOutputStream(rawOut);
	BinarySerialEncoder encoder = new BinarySerialEncoder(out);
	encoder.encode(node);
    }

    @Override
    public SerialNode decode(InputStream rawIn) throws IOException {
	DataInputStream in = new DataInputStream(rawIn);
	BinarySerialDecoder decoder = new BinarySerialDecoder(in);
	return decoder.decode();
    }
}
