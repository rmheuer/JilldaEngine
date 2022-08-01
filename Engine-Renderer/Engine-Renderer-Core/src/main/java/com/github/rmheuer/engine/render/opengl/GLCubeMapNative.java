package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.TextureFilter;

import static com.github.rmheuer.engine.render.opengl.GLEnumConversions.getGlTextureFilter;

public final class GLCubeMapNative implements CubeMap.Native {
    private final OpenGL gl;
    private final int id;

    public GLCubeMapNative(OpenGL gl) {
        this.gl = gl;
        id = gl.genTextures();
        gl.bindTexture(gl.TEXTURE_CUBE_MAP, id);
        gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_WRAP_R, gl.CLAMP_TO_EDGE);
        gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
        gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
    }

    @Override
    public void setPosXImage(Image posX) {
        gl.bindTexture(gl.TEXTURE_CUBE_MAP, id);
        gl.texImage2D(gl.TEXTURE_CUBE_MAP_POSITIVE_X, 0, gl.RGBA, posX.getWidth(), posX.getHeight(), 0, gl.RGBA, gl.UNSIGNED_BYTE, posX.getRgbaData());
    }

    @Override
    public void setNegXImage(Image negX) {
        gl.bindTexture(gl.TEXTURE_CUBE_MAP, id);
        gl.texImage2D(gl.TEXTURE_CUBE_MAP_NEGATIVE_X, 0, gl.RGBA, negX.getWidth(), negX.getHeight(), 0, gl.RGBA, gl.UNSIGNED_BYTE, negX.getRgbaData());
    }

    @Override
    public void setPosYImage(Image posY) {
        gl.bindTexture(gl.TEXTURE_CUBE_MAP, id);
        gl.texImage2D(gl.TEXTURE_CUBE_MAP_POSITIVE_Y, 0, gl.RGBA, posY.getWidth(), posY.getHeight(), 0, gl.RGBA, gl.UNSIGNED_BYTE, posY.getRgbaData());
    }

    @Override
    public void setNegYImage(Image negY) {
        gl.bindTexture(gl.TEXTURE_CUBE_MAP, id);
        gl.texImage2D(gl.TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, gl.RGBA, negY.getWidth(), negY.getHeight(), 0, gl.RGBA, gl.UNSIGNED_BYTE, negY.getRgbaData());
    }

    @Override
    public void setPosZImage(Image posZ) {
        gl.bindTexture(gl.TEXTURE_CUBE_MAP, id);
        gl.texImage2D(gl.TEXTURE_CUBE_MAP_POSITIVE_Z, 0, gl.RGBA, posZ.getWidth(), posZ.getHeight(), 0, gl.RGBA, gl.UNSIGNED_BYTE, posZ.getRgbaData());
    }

    @Override
    public void setNegZImage(Image negZ) {
        gl.bindTexture(gl.TEXTURE_CUBE_MAP, id);
        gl.texImage2D(gl.TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, gl.RGBA, negZ.getWidth(), negZ.getHeight(), 0, gl.RGBA, gl.UNSIGNED_BYTE, negZ.getRgbaData());
    }

    @Override
    public void setFilters(TextureFilter minFilter, TextureFilter magFilter) {
        gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_MAG_FILTER, getGlTextureFilter(gl, magFilter));
        gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_MIN_FILTER, getGlTextureFilter(gl, minFilter));
    }

    @Override
    public void bindToSlot(int slot) {
        if (slot < 0 || slot >= 16) {
            throw new IndexOutOfBoundsException("Slot index out of bounds: " + slot);
        }

        gl.activeTexture(gl.TEXTURE0 + slot);
        gl.bindTexture(gl.TEXTURE_CUBE_MAP, id);
    }

    @Override
    public NativeObjectFreeFn getFreeFn() {
        return new GLDestructor(id, gl::deleteTextures);
    }
}
