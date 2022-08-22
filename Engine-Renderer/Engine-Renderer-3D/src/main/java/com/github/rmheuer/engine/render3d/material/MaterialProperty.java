package com.github.rmheuer.engine.render3d.material;

import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.Texture;

public final class MaterialProperty {
    private final String name;
    private Object value;

    public MaterialProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isTexture() { return value instanceof Texture; }

    public boolean isImage() {
        return value instanceof Image;
    }

    public boolean isCubeMap() {
        return value instanceof CubeMap;
    }

    public Texture getTexture() { return (Texture) value; }

    public Image getImage() {
        return (Image) value;
    }

    public CubeMap getCubeMap() {
        return (CubeMap) value;
    }

    public void setImage(Image image) {
        value = image;
    }

    public void setCubeMap(CubeMap map) {
        value = map;
    }
}
