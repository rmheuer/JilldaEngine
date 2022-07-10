package com.github.rmheuer.engine.render.opengl;

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

import static org.lwjgl.opengl.GL33C.*;

public final class OpenGLMesh<V extends Vertex> implements Mesh<V> {
    private final int vao;
    private final int vbo;
    private final int ibo;
    private final int primType;

    private boolean hasUploadedLayout;
    private int sizeOfVertex;
    private int indexCount;

    public OpenGLMesh(PrimitiveType primType) {
	vao = glGenVertexArrays();
	vbo = glGenBuffers();
	ibo = glGenBuffers();
	this.primType = getGLPrimType(primType);

	glBindVertexArray(vao);
	glBindBuffer(GL_ARRAY_BUFFER, vbo);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	glBindVertexArray(0);

	hasUploadedLayout = false;
    }

    @Override
    public void setData(List<V> vertices, List<Integer> indices, MeshDataUsage usage) {
	if (!hasUploadedLayout && !vertices.isEmpty()) {
	    Vertex firstVertex = vertices.get(0);

	    uploadLayout(firstVertex.getLayout());
	    sizeOfVertex = firstVertex.sizeOf();
	}

	ByteBuffer vertexBuf = storeVerticesToBuffer(vertices);
	IntBuffer indexBuf = storeIndicesToBuffer(indices);
	int glUsage = getGLUsage(usage);

	glBindVertexArray(vao);
	glBindBuffer(GL_ARRAY_BUFFER, vbo);
	glBufferData(GL_ARRAY_BUFFER, vertexBuf, glUsage);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuf, glUsage);
	glBindVertexArray(0);

	indexCount = indices.size();
    }

    @Override
    public void draw() {
	if (!hasUploadedLayout)
	    return;

	glBindVertexArray(vao);
	glDrawElements(primType, indexCount, GL_UNSIGNED_INT, 0L);
	glBindVertexArray(0);
    }

    @Override
    public void delete() {
	glDeleteBuffers(vbo);
	glDeleteBuffers(ibo);
	glDeleteVertexArrays(vao);
    }

    private int getGLPrimType(PrimitiveType type) {
	switch (type) {
	    case POINTS: return GL_POINTS;
	    case LINE_STRIP: return GL_LINE_STRIP;
	    case LINE_LOOP: return GL_LINE_LOOP;
	    case LINES: return GL_LINES;
	    case TRIANGLE_STRIP: return GL_TRIANGLE_STRIP;
	    case TRIANGLE_FAN: return GL_TRIANGLE_FAN;
	    case TRIANGLES: return GL_TRIANGLES;
	    default:
	        throw new IllegalArgumentException();
	}
    }

    private int getGLUsage(MeshDataUsage usage) {
	switch (usage) {
	    case STATIC: return GL_STATIC_DRAW;
	    case DYNAMIC: return GL_DYNAMIC_DRAW;
	    case STREAM: return GL_STREAM_DRAW;
	    default:
	        throw new IllegalArgumentException();
	}
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

    private ByteBuffer storeVerticesToBuffer(List<V> vertices) {
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
}
