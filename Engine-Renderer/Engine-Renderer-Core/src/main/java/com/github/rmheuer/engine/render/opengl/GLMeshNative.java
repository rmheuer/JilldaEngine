package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.core.util.SizeOf;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.mesh.Vertex;
import com.github.rmheuer.engine.render.mesh.VertexLayout;
import com.github.rmheuer.engine.render.shader.AttribType;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static com.github.rmheuer.engine.render.opengl.GLEnumConversions.getGlMeshDataUsage;
import static com.github.rmheuer.engine.render.opengl.GLEnumConversions.getGlPrimitiveType;
import static org.lwjgl.opengl.GL33C.*;

public final class GLMeshNative implements Mesh.Native {
    private final int vao, vbo, ibo;
    private final int primType;

    private boolean hasUploadedLayout;
    private int sizeOfVertex;

    private boolean renderUsingIndexBuffer;
    private int indexCount;

    public GLMeshNative(PrimitiveType primType) {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ibo = glGenBuffers();
        this.primType = getGlPrimitiveType(primType);

        // Attach buffers to VAO
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBindVertexArray(0);

        hasUploadedLayout = false;
    }

    @Override
    public void setData(List<? extends Vertex> vertices, List<Integer> indices, MeshDataUsage usage) {
        if (!hasUploadedLayout && !vertices.isEmpty()) {
            Vertex firstVertex = vertices.get(0);

            uploadLayout(firstVertex.getLayout());
            sizeOfVertex = firstVertex.sizeOf();
        }

        ByteBuffer vertexBuf = storeVerticesToBuffer(vertices);
        int glUsage = getGlMeshDataUsage(usage);

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuf, glUsage);
        if (indices != null) {
            IntBuffer indexBuf = storeIndicesToBuffer(indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuf, glUsage);
        }
        glBindVertexArray(0);

        if (indices != null) {
            indexCount = indices.size();
            renderUsingIndexBuffer = true;
        } else {
            indexCount = vertices.size();
            renderUsingIndexBuffer = false;
        }
    }

    @Override
    public void render() {
        // If there is no layout, there is no data, so don't render
        if (!hasUploadedLayout)
            return;

        glBindVertexArray(vao);
        if (renderUsingIndexBuffer)
            glDrawElements(primType, indexCount, GL_UNSIGNED_INT, 0L);
        else
            glDrawArrays(primType, 0, indexCount);
        glBindVertexArray(0);
    }

    private ByteBuffer storeVerticesToBuffer(List<? extends Vertex> vertices) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(vertices.size() * sizeOfVertex);

        for (Vertex v : vertices)
            v.addToBuffer(buffer);

        buffer.flip();
        return buffer;
    }

    private IntBuffer storeIndicesToBuffer(List<Integer> indices) {
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.size());

        for (int i : indices)
            buffer.put(i);

        buffer.flip();
        return buffer;
    }

    private void uploadLayout(VertexLayout vertexLayout) {
        AttribType[] layout = vertexLayout.getTypes();

        int stride = 0;
        for (AttribType type : layout)
            stride += type.getElemCount() * SizeOf.FLOAT;

        int offset = 0;
        glBindVertexArray(vao);
        for (int i = 0; i < layout.length; i++) {
            AttribType type = layout[i];
            glVertexAttribPointer(
                    i,
                    type.getElemCount(),
                    GL_FLOAT,
                    false, /* Normalized */
                    stride,
                    offset
            );
            glEnableVertexAttribArray(i);
            offset += type.getElemCount() * SizeOf.FLOAT;
        }
        glBindVertexArray(0);

        hasUploadedLayout = true;
    }

    private static final class Destructor implements NativeObjectFreeFn {
        private final int vao, vbo, ibo;

        public Destructor(int vao, int vbo, int ibo) {
            this.vao = vao;
            this.vbo = vbo;
            this.ibo = ibo;
        }

        @Override
        public void free() {
            glDeleteBuffers(vbo);
            glDeleteBuffers(ibo);
            glDeleteVertexArrays(vao);
        }
    }

    @Override
    public NativeObjectFreeFn getFreeFn() {
        return new Destructor(vao, vbo, ibo);
    }
}
