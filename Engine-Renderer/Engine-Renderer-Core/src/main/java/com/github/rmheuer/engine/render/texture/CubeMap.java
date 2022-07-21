package com.github.rmheuer.engine.render.texture;

public abstract class CubeMap extends Texture {
    public abstract TextureSettings getSettings();

    public abstract TextureData getPositiveXData();
    public abstract TextureData getNegativeXData();
    public abstract TextureData getPositiveYData();
    public abstract TextureData getNegativeYData();
    public abstract TextureData getPositiveZData();
    public abstract TextureData getNegativeZData();
}
