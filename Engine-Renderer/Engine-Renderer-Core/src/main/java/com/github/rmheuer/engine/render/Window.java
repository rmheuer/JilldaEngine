package com.github.rmheuer.engine.render;

import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.render.framebuffer.Framebuffer;

public interface Window {
    void update();

    int getWidth();
    int getHeight();
    Vector2i getSize();

    Framebuffer getDefaultFramebuffer();
    
    void close();
}
