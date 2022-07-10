package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.render.framebuffer.Framebuffer;

public class OpenGLFramebuffer implements Framebuffer {
    private final int id;

    public OpenGLFramebuffer() {
        this(0);
        // TODO
    }

    protected OpenGLFramebuffer(int id) {
        this.id = id;
    }

    @Override
    public void bind() {
        // TODO
    }

    @Override
    public void unbind() {
        // TODO
    }

    @Override
    public Vector2i getSize() {
        return new Vector2i(0, 0);
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public void delete() {
        // TODO
    }
}
