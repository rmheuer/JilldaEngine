package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.render.RenderConstants;
import com.github.rmheuer.engine.render.mesh.MeshData;
import com.github.rmheuer.engine.render.mesh.VertexLayout;
import com.github.rmheuer.engine.render.shader.AttribType;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.Subimage;

import java.util.List;

public final class VertexBatch {
    public static final VertexLayout LAYOUT = new VertexLayout(
            AttribType.VEC3, // Position
            AttribType.VEC2, // Texture coord
            AttribType.VEC4, // Color
            AttribType.FLOAT // Texture slot
    );

    private final Image[] textures;
    private final MeshData data;

    public VertexBatch() {
        textures = new Image[RenderConstants.MAX_TEXTURE_SLOTS];
        data = new MeshData(LAYOUT);
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

        data.putVec3(v.getPos())
                .putVec2(v.getU(), v.getV())
                .putVec4(v.getColor())
                .putFloat(textureSlot);

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

    // Transfers ownership of the data to the caller
    public MeshData getData() {
        return data;
    }
}
