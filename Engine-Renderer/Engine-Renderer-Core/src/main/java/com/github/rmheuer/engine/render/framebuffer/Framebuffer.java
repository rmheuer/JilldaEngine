package com.github.rmheuer.engine.render.framebuffer;

import com.github.rmheuer.engine.core.math.Vector2i;

public interface Framebuffer {
    void bind();
    void unbind();

    Vector2i getSize();

    boolean isDefault();

    void delete();
}
