package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.TextureFilter;
import org.lwjgl.opengl.GL33C;

import static com.github.rmheuer.engine.render.opengl.GLEnumConversions.getGlTextureFilter;
import static org.lwjgl.opengl.GL33C.*;

public final class GLImageNative implements Image.Native {
    private final int width;
    private final int height;
    private final int id;

    public GLImageNative(int width, int height) {
        this.width = width;
        this.height = height;
        id = glGenTextures();
    }

    @Override
    public void setData(int[] rgbaData) {
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, rgbaData);
    }

    @Override
    public void setFilters(TextureFilter minFilter, TextureFilter magFilter) {
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, getGlTextureFilter(minFilter));
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, getGlTextureFilter(magFilter));
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
    public NativeObjectFreeFn getFreeFn() {
        return new GLDestructor(id, GL33C::glDeleteTextures);
    }
}
