package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.render.mesh.MeshBuilder;
import com.github.rmheuer.engine.render.texture.Texture;

import java.util.List;

import static com.github.rmheuer.engine.render2d.Renderer2D.MAX_TEXTURE_SLOTS;

public final class VertexBatch {
    private final Texture[] textures;
    private final MeshBuilder<Vertex2D> data;

    public VertexBatch() {
        textures = new Texture[MAX_TEXTURE_SLOTS];
        data = new MeshBuilder<>();
    }

    public boolean addVertex(DrawVertex v, Texture defaultTexture) {
        Texture tex = v.getTex();
        if (tex == null)
            tex = defaultTexture;

        int textureSlot = -1;
        for (int i = 0; i < MAX_TEXTURE_SLOTS; i++) {
            if (textures[i] == null) {
                textures[i] = tex;
                textureSlot = i;
                break;
            }

            if (textures[i].equals(tex)) {
                textureSlot = i;
                break;
            }
        }
        if (textureSlot == -1)
            return false;

        data.vertex(new Vertex2D(v.getX(), v.getY(), v.getDepth(), v.getU(), v.getV(), v.getColor(), textureSlot));

        return true;
    }

    public void addIndex(int index) {
        data.index(index);
    }

    public void addIndices(List<Integer> indices) {
        for (int i : indices) {
            data.index(i);
        }
    }

    public Texture[] getTextures() {
        return textures;
    }

    public MeshBuilder<Vertex2D> getData() {
        return data;
    }
}
