package com.github.rmheuer.engine.render3d.material;

import com.github.rmheuer.engine.core.asset.Asset;
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

    public Texture2D getTexture2D() {
        return (Texture2D) value;
    }

    public void setTexture2D(Texture2D texture) {
        releaseValue();
        value = texture;
        texture.claim();
    }

    private void releaseValue() {
        if (value instanceof Asset) {
            Asset asset = (Asset) value;
            asset.release();
        }
    }
}
