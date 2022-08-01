package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.TextureFilter;
import org.lwjgl.opengl.GL33C;

import static com.github.rmheuer.engine.render.opengl.GLEnumConversions.getGlTextureFilter;

public final class GLImageNative implements Image.Native {
    private final OpenGL gl;
    private final int width;
    private final int height;
    private final int id;

    public GLImageNative(OpenGL gl, int width, int height) {
        this.gl = gl;
        this.width = width;
        this.height = height;
        id = gl.genTextures();
    }

    @Override
    public void setData(int[] rgbaData) {
        gl.bindTexture(gl.TEXTURE_2D, id);
        gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, width, height, 0, gl.RGBA, gl.UNSIGNED_BYTE, rgbaData);
    }

    @Override
    public void setFilters(TextureFilter minFilter, TextureFilter magFilter) {
        gl.bindTexture(gl.TEXTURE_2D, id);
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, getGlTextureFilter(gl, minFilter));
        gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, getGlTextureFilter(gl, magFilter));
    }

    @Override
    public void bindToSlot(int slot) {
        if (slot < 0 || slot >= 16) {
            throw new IndexOutOfBoundsException("Slot index out of bounds: " + slot);
        }

        gl.activeTexture(gl.TEXTURE0 + slot);
        gl.bindTexture(gl.TEXTURE_2D, id);
    }

    @Override
    public NativeObjectFreeFn getFreeFn() {
        return new GLDestructor(id, gl::deleteTextures);
    }
}
