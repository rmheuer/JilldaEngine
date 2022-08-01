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

public final class GLMeshNative implements Mesh.Native {
    private final OpenGL gl;
    private final int vao, vbo, ibo;
    private final int primType;

    private boolean hasUploadedLayout;
    private int sizeOfVertex;

    private boolean renderUsingIndexBuffer;
    private int indexCount;

    public GLMeshNative(OpenGL gl, PrimitiveType primType) {
        this.gl = gl;
        vao = gl.genVertexArrays();
        vbo = gl.genBuffers();
        ibo = gl.genBuffers();
        this.primType = getGlPrimitiveType(gl, primType);

        // Attach buffers to VAO
        gl.bindVertexArray(vao);
        gl.bindBuffer(gl.ARRAY_BUFFER, vbo);
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, ibo);
        gl.bindVertexArray(0);

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
        int glUsage = getGlMeshDataUsage(gl, usage);

        gl.bindVertexArray(vao);
        gl.bindBuffer(gl.ARRAY_BUFFER, vbo);
        gl.bufferData(gl.ARRAY_BUFFER, vertexBuf, glUsage);
        if (indices != null) {
            IntBuffer indexBuf = storeIndicesToBuffer(indices);
            gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, ibo);
            gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, indexBuf, glUsage);
        }
        gl.bindVertexArray(0);

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

        gl.bindVertexArray(vao);
        if (renderUsingIndexBuffer)
            gl.drawElements(primType, indexCount, gl.UNSIGNED_INT, 0L);
        else
            gl.drawArrays(primType, 0, indexCount);
        gl.bindVertexArray(0);
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
        gl.bindVertexArray(vao);
        for (int i = 0; i < layout.length; i++) {
            AttribType type = layout[i];
            gl.vertexAttribPointer(
                    i,
                    type.getElemCount(),
                    gl.FLOAT,
                    false, /* Normalized */
                    stride,
                    offset
            );
            gl.enableVertexAttribArray(i);
            offset += type.getElemCount() * SizeOf.FLOAT;
        }
        gl.bindVertexArray(0);

        hasUploadedLayout = true;
    }

    private static final class Destructor implements NativeObjectFreeFn {
        private final OpenGL gl;
        private final int vao, vbo, ibo;

        public Destructor(OpenGL gl, int vao, int vbo, int ibo) {
            this.gl = gl;
            this.vao = vao;
            this.vbo = vbo;
            this.ibo = ibo;
        }

        @Override
        public void free() {
            gl.deleteBuffers(vbo);
            gl.deleteBuffers(ibo);
            gl.deleteVertexArrays(vao);
        }
    }

    @Override
    public NativeObjectFreeFn getFreeFn() {
        return new Destructor(gl, vao, vbo, ibo);
    }
}
