package com.github.rmheuer.engine.render;

import com.github.rmheuer.engine.core.math.Vector2i;

public interface Window {
    void update();

    int getWidth();
    int getHeight();
    Vector2i getSize();
    
    void close();
}
