package com.github.rmheuer.engine.render.texture;

import com.github.rmheuer.engine.core.asset.Asset;

public abstract class Texture2D extends Asset implements Texture {
    public abstract int getWidth();
    public abstract int getHeight();

    public abstract TextureSettings getSettings();
    public abstract TextureData getSourceData();
}
