package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.render.RenderConstants;
import com.github.rmheuer.engine.render.mesh.MeshBuilder;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.Subimage;

import java.util.List;

public final class VertexBatch {
    private final Image[] textures;
    private final MeshBuilder<Vertex2D> data;

    public VertexBatch() {
        textures = new Image[RenderConstants.MAX_TEXTURE_SLOTS];
        data = new MeshBuilder<>();
    }

    public boolean addVertex(DrawVertex v, Image defaultTexture) {
        Subimage texSub = v.getTex();
        Image tex;
        if (texSub == null)
            tex = defaultTexture;
        else
            tex = texSub.getSourceImage();

        int textureSlot = -1;
        for (int i = 0; i < RenderConstants.MAX_TEXTURE_SLOTS; i++) {
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

        data.vertex(new Vertex2D(v.getPos(), v.getU(), v.getV(), v.getColor(), textureSlot));

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

    public Image[] getTextures() {
        return textures;
    }

    public MeshBuilder<Vertex2D> getData() {
        return data;
    }
}
