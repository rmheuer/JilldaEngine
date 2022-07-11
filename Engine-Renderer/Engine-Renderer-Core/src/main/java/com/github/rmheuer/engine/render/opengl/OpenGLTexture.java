package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.texture.Texture;
import com.github.rmheuer.engine.render.texture.TextureData;
import com.github.rmheuer.engine.render.texture.TextureFilter;
import com.github.rmheuer.engine.render.texture.TextureSettings;

import java.io.IOException;

import static org.lwjgl.opengl.GL33C.*;

public final class OpenGLTexture extends Texture {
    private final int id;
    private final int width;
    private final int height;
    private final TextureSettings settings;
    private final TextureData data;

    public OpenGLTexture(ResourceFile res, TextureSettings settings) throws IOException {
        this(TextureData.decode(res), settings);
    }

    public OpenGLTexture(TextureData data, TextureSettings settings) {
        this.data = data;
        width = data.getWidth();
        height = data.getHeight();
        this.settings = settings;

        data.claim();

        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, getGlFilter(settings.getMinFilter()));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, getGlFilter(settings.getMagFilter()));
        glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                data.getWidth(),
                data.getHeight(),
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data.getPixels()
        );
    }

    private int getGlFilter(TextureFilter filter) {
        switch (filter) {
            case LINEAR:
                return GL_LINEAR;
            case NEAREST:
                return GL_NEAREST;
            default:
                throw new IllegalArgumentException(String.valueOf(filter));
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void bindToSlot(int slot) {
        if (slot < 0 || slot >= 16) {
            throw new IndexOutOfBoundsException("Slot index out of bounds: " + slot);
        }

        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    @Override
    public TextureSettings getSettings() {
        return settings;
    }

    @Override
    public TextureData getSourceData() {
        return data;
    }

    @Override
    public void freeAsset() {
        glDeleteTextures(id);
        data.release();
    }
}
