package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.render.Window;

public final class OpenGLWindowFramebuffer extends OpenGLFramebuffer {
    private final Window window;

    public OpenGLWindowFramebuffer(Window window) {
        super(0);
        this.window = window;
    }

    @Override
    public Vector2i getSize() {
        return window.getSize();
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public void delete() {
        // Can't delete the default framebuffer
    }
}
