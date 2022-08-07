package com.github.rmheuer.engine.core.serial2.json;

import com.github.rmheuer.engine.core.serial2.SerialCodec;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

public final class JsonCodec implements SerialCodec {
    private static final JsonCodec INSTANCE = new JsonCodec();
    public static JsonCodec get() { return INSTANCE; }

    private JsonCodec() {}

    public String encode(SerialNode node) {
        StringWriter writer = new StringWriter();
        JsonEncoder encoder = new JsonEncoder(writer);
        try {
            encoder.encode(node);
        } catch (IOException e) {
            throw new AssertionError("StringWriter threw IOException somehow", e);
        }
        return writer.toString();
    }

    public SerialNode decode(String str) {
        try {
            StringReader reader = new StringReader(str);
            JsonDecoder decoder = new JsonDecoder(reader);
            return decoder.decode();
        } catch (IOException e) {
            throw new IllegalArgumentException("Malformed JSON input", e);
        }
    }

    @Override
    public void encode(SerialNode node, OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        JsonEncoder encoder = new JsonEncoder(writer);
        encoder.encode(node);
        writer.close();
    }

    @Override
    public SerialNode decode(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);
        JsonDecoder decoder = new JsonDecoder(reader);
        SerialNode node = decoder.decode();
        reader.close();
        return node;
    }
}
