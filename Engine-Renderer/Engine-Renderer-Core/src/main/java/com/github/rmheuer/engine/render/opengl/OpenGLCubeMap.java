package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.TextureData;
import com.github.rmheuer.engine.render.texture.TextureSettings;

import static org.lwjgl.opengl.GL33C.*;

public final class OpenGLCubeMap extends CubeMap {
    private final int id;
    private final TextureSettings settings;
    private final TextureData posX, negX, posY, negY, posZ, negZ;

    public OpenGLCubeMap(TextureSettings settings, TextureData posX, TextureData negX, TextureData posY, TextureData negY, TextureData posZ, TextureData negZ) {
        this.settings = settings;
        this.posX = posX; posX.claim();
        this.negX = negX; negX.claim();
        this.posY = posY; posY.claim();
        this.negY = negY; negY.claim();
        this.posZ = posZ; posZ.claim();
        this.negZ = negZ; negZ.claim();

        id = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);

        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA, posX.getWidth(), posX.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, posX.getPixels());
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGBA, negX.getWidth(), negX.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, negX.getPixels());
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGBA, posY.getWidth(), posY.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, posY.getPixels());
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGBA, negY.getWidth(), negY.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, negY.getPixels());
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGBA, posZ.getWidth(), posZ.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, posZ.getPixels());
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGBA, negZ.getWidth(), negZ.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, negZ.getPixels());

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, OpenGLTexture2D.getGlFilter(settings.getMagFilter()));
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, OpenGLTexture2D.getGlFilter(settings.getMinFilter()));
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    @Override
    public void bindToSlot(int slot) {
        if (slot < 0 || slot >= 16) {
            throw new IndexOutOfBoundsException("Slot index out of bounds: " + slot);
        }

        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
    }

    @Override
    public TextureSettings getSettings() {
        return settings;
    }

    @Override
    public TextureData getPositiveXData() {
        return posX;
    }

    @Override
    public TextureData getNegativeXData() {
        return negX;
    }

    @Override
    public TextureData getPositiveYData() {
        return posY;
    }

    @Override
    public TextureData getNegativeYData() {
        return negY;
    }

    @Override
    public TextureData getPositiveZData() {
        return posZ;
    }

    @Override
    public TextureData getNegativeZData() {
        return negZ;
    }

    @Override
    protected void freeAsset() {
        posX.release();
        negX.release();
        posY.release();
        negY.release();
        posZ.release();
        negZ.release();
        glDeleteTextures(id);
    }
}
