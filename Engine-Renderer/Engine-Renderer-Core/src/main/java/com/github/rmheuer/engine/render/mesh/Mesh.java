package com.github.rmheuer.engine.render.mesh;

import java.util.List;

public interface Mesh<V extends Vertex> {
    void setData(List<V> vertices, List<Integer> indices, MeshDataUsage usage);

    void draw();

    void delete();

    default void setData(MeshBuilder<V> builder, MeshDataUsage usage) {
	setData(builder.getVertices(), builder.getIndices(), usage);
    }
}
