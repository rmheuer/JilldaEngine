package com.github.rmheuer.engine.render3d.loader;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.util.Pair;
import com.github.rmheuer.engine.render.mesh.Vertex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ObjLoader {
    public static Pair<List<DefaultVertex>, List<Integer>> loadObj(ResourceFile source) throws IOException {
        return loadObj(source, DefaultVertex::new);
    }

    private static final class IndexGroup {
        int positionIdx = -1;
        int textureIdx = -1;
        int normalIdx = -1;

        public IndexGroup(String str) {
            String[] parts = str.split("/");
            int len = parts.length;

            positionIdx = Integer.parseInt(parts[0]) - 1;
            if (len > 1)
                textureIdx = parts[1].length() > 0 ? Integer.parseInt(parts[1]) - 1 : -1;
            if (len > 2)
                normalIdx = Integer.parseInt(parts[2]) - 1;
        }
    }

    private static final class Face {
        IndexGroup v1, v2, v3;

        public Face(String v1, String v2, String v3) {
            this.v1 = new IndexGroup(v1);
            this.v2 = new IndexGroup(v2);
            this.v3 = new IndexGroup(v3);
        }
    }

    private static final class VertexData {
        Vector3f position;
        Vector2f texCoord;
        Vector3f normal;

        public VertexData(Vector3f position, Vector2f texCoord, Vector3f normal) {
            this.position = position;
            this.texCoord = texCoord;
            this.normal = normal;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VertexData that = (VertexData) o;
            return Objects.equals(position, that.position) &&
                    Objects.equals(texCoord, that.texCoord) &&
                    Objects.equals(normal, that.normal);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, texCoord, normal);
        }
    }

    private static final class ObjData {
        List<Vector3f> positions;
        List<Vector2f> texCoords;
        List<Vector3f> normals;

        public ObjData(List<Vector3f> positions, List<Vector2f> texCoords, List<Vector3f> normals) {
            this.positions = positions;
            this.texCoords = texCoords;
            this.normals = normals;
        }
    }

    private static void addVertex(IndexGroup idx, ObjData data, List<VertexData> vertices, List<Integer> indices) {
        Vector3f position = data.positions.get(idx.positionIdx);
        Vector2f texCoord = idx.textureIdx != -1 ? data.texCoords.get(idx.textureIdx) : new Vector2f(0, 0);
        Vector3f normal = idx.normalIdx != -1 ? data.normals.get(idx.normalIdx) : new Vector3f(0, 0, 0);

        VertexData vtx = new VertexData(position, texCoord, normal);
        int existingIdx = vertices.indexOf(vtx);
        if (existingIdx >= 0) {
            indices.add(existingIdx);
        } else {
            indices.add(vertices.size());
            vertices.add(vtx);
        }
    }

    public static <V extends Vertex> Pair<List<V>, List<Integer>> loadObj(ResourceFile source, VertexAdapter<V> adapter) throws IOException {
        String str = source.readAsString();
        String[] lines = str.split("\n");

        List<Vector3f> positions = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.trim().split("\\s+");
            switch (tokens[0]) {
                case "v":
                    positions.add(new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    ));
                    break;
                case "vt":
                    texCoords.add(new Vector2f(
                            Float.parseFloat(tokens[1]),
                            1 - Float.parseFloat(tokens[2]) // Flip y coordinate here
                    ));
                    break;
                case "vn":
                    normals.add(new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    ));
                    break;
                case "f":
                    faces.add(new Face(tokens[1], tokens[2], tokens[3]));
                    break;
            }
        }

        List<VertexData> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        ObjData obj = new ObjData(positions, texCoords, normals);
        for (Face face : faces) {
            addVertex(face.v1, obj, vertices, indices);
            addVertex(face.v2, obj, vertices, indices);
            addVertex(face.v3, obj, vertices, indices);
        }

        List<V> vertexOut = new ArrayList<>();
        for (VertexData vertex : vertices) {
            vertexOut.add(adapter.adapt(vertex.position, vertex.texCoord, vertex.normal));
        }

        return new Pair<>(vertexOut, indices);
    }
}
