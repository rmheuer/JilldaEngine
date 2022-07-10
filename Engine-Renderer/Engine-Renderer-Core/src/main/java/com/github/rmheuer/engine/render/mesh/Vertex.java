package com.github.rmheuer.engine.render.mesh;

import java.nio.ByteBuffer;

public interface Vertex {
    int sizeOf();

    VertexLayout getLayout();

    void addToBuffer(ByteBuffer buf);
}
