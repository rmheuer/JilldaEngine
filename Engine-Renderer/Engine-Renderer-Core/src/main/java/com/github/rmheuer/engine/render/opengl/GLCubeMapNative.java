package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.TextureFilter;
import org.lwjgl.opengl.GL33C;

import static com.github.rmheuer.engine.render.opengl.GLEnumConversions.getGlTextureFilter;
import static org.lwjgl.opengl.GL33C.*;

public final class GLCubeMapNative implements CubeMap.Native {
    private final int id;

    public GLCubeMapNative() {
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    @Override
    public void setPosXImage(Image posX) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA, posX.getWidth(), posX.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, posX.getRgbaData());
    }

    @Override
    public void setNegXImage(Image negX) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGBA, negX.getWidth(), negX.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, negX.getRgbaData());
    }

    @Override
    public void setPosYImage(Image posY) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGBA, posY.getWidth(), posY.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, posY.getRgbaData());
    }

    @Override
    public void setNegYImage(Image negY) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGBA, negY.getWidth(), negY.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, negY.getRgbaData());
    }

    @Override
    public void setPosZImage(Image posZ) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGBA, posZ.getWidth(), posZ.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, posZ.getRgbaData());
    }

    @Override
    public void setNegZImage(Image negZ) {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGBA, negZ.getWidth(), negZ.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, negZ.getRgbaData());
    }

    @Override
    public void setFilters(TextureFilter minFilter, TextureFilter magFilter) {
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, getGlTextureFilter(magFilter));
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, getGlTextureFilter(minFilter));
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
    public NativeObjectFreeFn getFreeFn() {
        return new GLDestructor(id, GL33C::glDeleteTextures);
    }
}
