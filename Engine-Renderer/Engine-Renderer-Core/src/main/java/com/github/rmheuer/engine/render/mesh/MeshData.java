package com.github.rmheuer.engine.render.mesh;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.render.shader.AttribType;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class MeshData implements AutoCloseable {
    private static final int INITIAL_CAPACITY = 16;

    private final VertexLayout layout;
    private int layoutElemIdx;

    private ByteBuffer vertexBuf;
    private final List<Integer> indices; // TODO: FastUtils this one
    private int mark;

    public MeshData(VertexLayout layout) {
        this.layout = layout;
        layoutElemIdx = 0;

        indices = new ArrayList<>();
        mark = 0;

        vertexBuf = MemoryUtil.memAlloc(INITIAL_CAPACITY * layout.sizeOf());
    }

    private void ensureSpace(int spaceBytes) {
        if (vertexBuf.remaining() < spaceBytes) {
            vertexBuf = MemoryUtil.memRealloc(vertexBuf, vertexBuf.capacity() * 2);
        }
    }

    // TODO: This may need to be optimized since it's called a lot
    private void prepare(AttribType type) {
        AttribType[] types = layout.getTypes();

        ensureSpace(type.sizeOf());
        AttribType layoutType = types[layoutElemIdx++];
        if (layoutType != type)
            throw new IllegalStateException("Incorrect attribute added for format (added " + type + ", layout specifies " + layoutType + ")");
        if (layoutElemIdx >= types.length)
            layoutElemIdx = 0;
    }

    public MeshData mark() {
        mark = getVertexCount();
        return this;
    }

    public MeshData putFloat(float f) {
        prepare(AttribType.FLOAT);
        vertexBuf.putFloat(f);
        return this;
    }

    public MeshData putVec2(Vector2f vec) { return putVec2(vec.x, vec.y); }
    public MeshData putVec2(float x, float y) {
        prepare(AttribType.VEC2);
        vertexBuf.putFloat(x);
        vertexBuf.putFloat(y);
        return this;
    }

    public MeshData putVec3(Vector3f vec) { return putVec3(vec.x, vec.y, vec.z); }
    public MeshData putVec3(float x, float y, float z) {
        prepare(AttribType.VEC3);
        vertexBuf.putFloat(x);
        vertexBuf.putFloat(y);
        vertexBuf.putFloat(z);
        return this;
    }

    public MeshData putVec4(Vector4f vec) { return putVec4(vec.x, vec.y, vec.z, vec.w); }
    public MeshData putVec4(float x, float y, float z, float w) {
        prepare(AttribType.VEC4);
        vertexBuf.putFloat(x);
        vertexBuf.putFloat(y);
        vertexBuf.putFloat(z);
        vertexBuf.putFloat(w);
        return this;
    }

    public MeshData index(int i) {
        indices.add(mark + i);
        return this;
    }

    public MeshData indices(int... indices) {
        for (int i : indices) {
            this.indices.add(mark + i);
        }
        return this;
    }

    public MeshData indices(List<Integer> indices) {
        for (int i : indices) {
            this.indices.add(mark + i);
        }
        return this;
    }

    public MeshData append(MeshData other) {
        if (!layout.equals(other.layout))
            throw new IllegalArgumentException("Can only append builder with same layout");

        mark();
        other.vertexBuf.flip();
        vertexBuf.put(other.vertexBuf);
        other.vertexBuf.position(other.vertexBuf.limit());
        other.vertexBuf.limit(other.vertexBuf.capacity());
        for (int i : other.indices) {
            indices.add(mark + i);
        }
        return this;
    }

    // Do not free the returned buffer
    public ByteBuffer getVertexBuf() {
        return vertexBuf;
    }

    public VertexLayout getVertexLayout() {
        return layout;
    }

    public int getVertexCount() {
        return vertexBuf.position() / layout.sizeOf();
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public int getMark() {
        return mark;
    }

    @Override
    public void close() {
        MemoryUtil.memFree(vertexBuf);
    }
}
