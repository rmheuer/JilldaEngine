package com.github.rmheuer.engine.render.texture;

public interface Texture {
    void bindToSlot(int slot);

    int getWidth();
    int getHeight();
    
    void delete();
}
