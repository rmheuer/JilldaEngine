package com.github.rmheuer.engine.core.serial.codec.json;

import com.github.rmheuer.engine.core.serial.codec.SerialCodec;
import com.github.rmheuer.engine.core.serial.node.SerialNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public final class JsonSerialCodec implements SerialCodec {
    private static final JsonSerialCodec INSTANCE = new JsonSerialCodec();

    public static JsonSerialCodec get() {
        return INSTANCE;
    }

    public static final String REFERENCE_KEY_NAME = "\"$ref\"";
    public static final String REFERENCE_ID_KEY_NAME = "\"$refId\"";
    public static final String REFERENCE_ID_VALUE_NAME = "\"$val\"";
    public static final String PATH_SEPARATOR = "/";

    @Override
    public void encode(SerialNode node, OutputStream out) throws IOException {
        Writer writer = new OutputStreamWriter(out);
        JsonEncoder encoder = new JsonEncoder(writer);
        encoder.encode(node);
        encoder.close();
    }

    @Override
    public SerialNode decode(InputStream in) throws IOException {
        Reader reader = new InputStreamReader(in);
        JsonDecoder decoder = new JsonDecoder(reader);
        SerialNode node = decoder.decode();
        decoder.close();
        return node;
    }
}
