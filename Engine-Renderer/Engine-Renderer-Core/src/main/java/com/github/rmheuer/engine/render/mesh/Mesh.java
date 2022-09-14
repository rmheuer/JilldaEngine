package com.github.rmheuer.engine.render.mesh;

import com.github.rmheuer.engine.core.nat.NativeObject;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.render.RenderBackend;

import java.util.List;

public final class Mesh<V extends Vertex> {
    private final PrimitiveType primType;

    private List<V> vertices;
    private List<Integer> indices;
    private MeshDataUsage dataUsage;

    private Native nat;
    private boolean dirty;

    public Mesh(PrimitiveType primType) {
        this.primType = primType;
        dirty = true;
    }

    public Mesh(PrimitiveType primType, List<V> vertices, MeshDataUsage usage) {
        this(primType, vertices, null, usage);
    }

    public Mesh(PrimitiveType primType, MeshBuilder<V> builder, MeshDataUsage usage) {
        this(primType, builder.getVertices(), builder.getIndices(), usage);
    }

    public Mesh(PrimitiveType primType, List<V> vertices, List<Integer> indices, MeshDataUsage usage) {
        this(primType);
        this.vertices = vertices;
        this.indices = indices;
        this.dataUsage = usage;
    }

    public PrimitiveType getPrimType() {
        return primType;
    }

    public void setData(MeshBuilder<V> builder, MeshDataUsage usage) {
        setData(builder.getVertices(), builder.getIndices(), usage);
    }

    public void setData(List<V> vertices, MeshDataUsage usage) {
        setData(vertices, null, usage);
    }

    public void setData(List<V> vertices, List<Integer> indices, MeshDataUsage usage) {
        this.vertices = vertices;
        this.indices = indices;
        this.dataUsage = usage;
        dirty = true;
    }

    public List<V> getVertices() {
        return vertices;
    }

    public void setVertices(List<V> vertices) {
        this.vertices = vertices;
        dirty = true;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
        dirty = true;
    }

    public MeshDataUsage getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(MeshDataUsage dataUsage) {
        this.dataUsage = dataUsage;
        dirty = true;
    }

    public boolean hasData() {
        return vertices != null;
    }

    public interface Native extends NativeObject {
        void setData(List<? extends Vertex> vertices, List<Integer> indices, MeshDataUsage usage);

        void render();
    }

    public Native getNative(NativeObjectManager mgr) {
        if (!hasData())
            throw new IllegalStateException("Cannot get native of mesh with no data");

        if (nat == null) {
            nat = RenderBackend.get().createMeshNative(primType);
            mgr.registerObject(nat);
        }

        if (dirty) {
            nat.setData(vertices, indices, dataUsage);
            dirty = false;
        }

        return nat;
    }
}
