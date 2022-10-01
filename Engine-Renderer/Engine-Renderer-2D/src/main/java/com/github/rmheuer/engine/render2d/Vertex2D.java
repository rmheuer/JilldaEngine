package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.core.util.SizeOf;
import com.github.rmheuer.engine.render.mesh.Vertex;
import com.github.rmheuer.engine.render.mesh.VertexLayout;
import com.github.rmheuer.engine.render.shader.AttribType;

import java.nio.ByteBuffer;

public final class Vertex2D implements Vertex {
    private static final int SIZEOF = 10 * SizeOf.FLOAT;
    private static final VertexLayout LAYOUT = new VertexLayout(
            AttribType.VEC3, // Position
            AttribType.VEC2, // Texture Coord
            AttribType.VEC4, // Color
            AttribType.FLOAT // Texture Slot
    );

    private final Vector3f position;
    private final Vector2f texCoord;
    private final Vector4f color;
    private final int textureSlot;

    public Vertex2D(Vector3f pos, float u, float v, Vector4f color, int textureSlot) {
        this(pos, new Vector2f(u, v), color, textureSlot);
    }

    public Vertex2D(Vector3f position, Vector2f texCoord, Vector4f color, int textureSlot) {
        this.position = position;
        this.texCoord = texCoord;
        this.color = color;
        this.textureSlot = textureSlot;
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
        color.put(buf);
        buf.putFloat(textureSlot);
    }
}
