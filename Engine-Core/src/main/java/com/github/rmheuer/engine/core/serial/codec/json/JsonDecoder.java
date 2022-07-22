package com.github.rmheuer.engine.core.serial.codec.json;

import com.github.rmheuer.engine.core.serial.node.SerialNode;

import java.io.IOException;
import java.io.Reader;

public final class JsonDecoder {
    private final Reader in;

    public JsonDecoder(Reader in) {
        this.in = in;
    }

    public SerialNode decode() throws IOException {
        return null;
    }

    public void close() throws IOException {
        in.close();
    }
}
