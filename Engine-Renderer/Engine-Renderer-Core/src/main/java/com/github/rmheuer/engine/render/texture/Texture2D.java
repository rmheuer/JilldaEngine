package com.github.rmheuer.engine.render.texture;

public abstract class Texture2D extends Texture {
    public abstract int getWidth();
    public abstract int getHeight();

    public abstract TextureSettings getSettings();
    public abstract TextureData getSourceData();
}
