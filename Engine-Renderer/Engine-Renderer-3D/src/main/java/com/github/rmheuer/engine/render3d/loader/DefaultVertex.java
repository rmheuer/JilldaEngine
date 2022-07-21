package com.github.rmheuer.engine.render3d.loader;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.util.SizeOf;
import com.github.rmheuer.engine.render.mesh.Vertex;
import com.github.rmheuer.engine.render.mesh.VertexLayout;
import com.github.rmheuer.engine.render.shader.AttribType;

import java.nio.ByteBuffer;

public final class DefaultVertex implements Vertex {
    private static final int SIZEOF = 8 * SizeOf.FLOAT;
    private static final VertexLayout LAYOUT = new VertexLayout(
            AttribType.VEC3, // Position
            AttribType.VEC2, // Texture coord
            AttribType.VEC3  // Normal
    );

    private final Vector3f position;
    private final Vector2f texCoord;
    private final Vector3f normal;

    public DefaultVertex(Vector3f position, Vector2f texCoord, Vector3f normal) {
        this.position = position;
        this.texCoord = texCoord;
        this.normal = normal;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getTexCoord() {
        return texCoord;
    }

    public Vector3f getNormal() {
        return normal;
    }

    @Override
    public int sizeOf() {
        return SIZEOF;
    }

    @Override
    public VertexLayout getLayout() {
        return LAYOUT;
    }

    @Override
    public void addToBuffer(ByteBuffer buf) {
        position.put(buf);
        texCoord.put(buf);
        normal.put(buf);
    }
}
