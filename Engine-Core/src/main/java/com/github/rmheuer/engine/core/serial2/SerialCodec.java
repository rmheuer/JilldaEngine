package com.github.rmheuer.engine.core.serial2;

import com.github.rmheuer.engine.core.serial2.node.SerialNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerialCodec {
    // Both encode and decode are expected to close their streams after using them
    void encode(SerialNode node, OutputStream out) throws IOException;
    SerialNode decode(InputStream in) throws IOException;
}
