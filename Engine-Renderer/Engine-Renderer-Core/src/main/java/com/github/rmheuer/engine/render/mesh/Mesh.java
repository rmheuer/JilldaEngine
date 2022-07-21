package com.github.rmheuer.engine.render.mesh;

import com.github.rmheuer.engine.core.asset.Asset;

import java.util.List;

public abstract class Mesh<V extends Vertex> extends Asset {
    public abstract void setData(List<V> vertices, List<Integer> indices, MeshDataUsage usage);

    public abstract void draw();

    public void setData(MeshBuilder<V> builder, MeshDataUsage usage) {
	setData(builder.getVertices(), builder.getIndices(), usage);
    }
}
