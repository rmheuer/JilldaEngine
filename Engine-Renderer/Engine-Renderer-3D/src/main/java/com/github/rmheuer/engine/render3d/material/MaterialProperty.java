package com.github.rmheuer.engine.render3d.material;

import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;

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
        return value instanceof Image;
    }

    public boolean isCubeMap() {
        return value instanceof CubeMap;
    }

    public Image getTexture2D() {
        return (Image) value;
    }

    public CubeMap getCubeMap() {
        return (CubeMap) value;
    }

    public void setTexture2D(Image texture) {
        value = texture;
    }

    public void setCubeMap(CubeMap map) {
        value = map;
    }
}
