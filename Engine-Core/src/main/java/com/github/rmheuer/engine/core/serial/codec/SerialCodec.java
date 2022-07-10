package com.github.rmheuer.engine.core.serial.codec;

import com.github.rmheuer.engine.core.serial.node.SerialNode;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface SerialCodec {
    void encode(SerialNode node, OutputStream out) throws IOException;

    SerialNode decode(InputStream in) throws IOException;
}
