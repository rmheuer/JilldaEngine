package com.github.rmheuer.engine.render3d.material;

import com.github.rmheuer.engine.core.asset.Asset;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Texture2D;

public final class MaterialProperty {
    private final String name;
    private Object value;

    public MaterialProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isTexture2D() {
        return value instanceof Texture2D;
    }

    public boolean isCubeMap() {
        return value instanceof CubeMap;
    }

    public Texture2D getTexture2D() {
        return (Texture2D) value;
    }

    public CubeMap getCubeMap() {
        return (CubeMap) value;
    }

    public void setTexture2D(Texture2D texture) {
        releaseValue();
        value = texture;
        texture.claim();
    }

    public void setCubeMap(CubeMap map) {
        releaseValue();
        value = map;
        map.claim();
    }

    private void releaseValue() {
        if (value instanceof Asset) {
            Asset asset = (Asset) value;
            asset.release();
        }
    }
}
