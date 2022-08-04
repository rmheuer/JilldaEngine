package com.github.rmheuer.engine.render.mesh;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public final class MeshBuilder<V extends Vertex> {
    private final List<V> vertices;
    private final List<Integer> indices;
    private int mark;

    public MeshBuilder() {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();
        mark = 0;
    }

    public MeshBuilder<V> mark() {
        mark = vertices.size();
        return this;
    }

    public MeshBuilder<V> vertex(V vertex) {
        vertices.add(vertex);
        return this;
    }

    @SafeVarargs
    public final MeshBuilder<V> vertices(V... vertices) {
        this.vertices.addAll(Arrays.asList(vertices));
        return this;
    }

    public MeshBuilder<V> index(int i) {
        indices.add(mark + i);
        return this;
    }

    public MeshBuilder<V> indices(int... indices) {
        for (int i : indices) {
            this.indices.add(mark + i);
        }
        return this;
    }

    public MeshBuilder<V> indices(List<Integer> indices) {
        for (int i : indices) {
            this.indices.add(mark + i);
        }
        return this;
    }

    public MeshBuilder<V> append(MeshBuilder<V> other) {
        mark();
        vertices.addAll(other.vertices);
        for (int i : other.indices) {
            indices.add(mark + i);
        }
        return this;
    }

    public List<V> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public int getMark() {
        return mark;
    }
}
