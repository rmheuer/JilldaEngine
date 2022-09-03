package com.github.rmheuer.engine.render3d.material;

import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Material {
    private final Map<String, MaterialProperty> properties;
    private ShaderProgram shader;

    public Material() {
        this(null);
    }

    public Material(ShaderProgram shader) {
        properties = new HashMap<>();
        this.shader = shader;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public Material setShader(ShaderProgram shader) {
        this.shader = shader;
        return this;
    }

    public MaterialProperty getProperty(String key) {
        return properties.get(key);
    }

    public Image getImage(String key) {
        return getProperty(key).getImage();
    }

    public CubeMap getCubeMap(String key) { return getProperty(key).getCubeMap(); }

    public float getFloat(String key) { return getProperty(key).getFloat(); }

    private MaterialProperty getOrCreateProperty(String key) {
        return properties.computeIfAbsent(key, MaterialProperty::new);
    }

    public Material setImage(String key, Image image) {
        getOrCreateProperty(key).setImage(image);
        return this;
    }

    public Material setCubeMap(String key, CubeMap map) {
        getOrCreateProperty(key).setCubeMap(map);
        return this;
    }

    public Material setFloat(String key, float f) {
        getOrCreateProperty(key).setFloat(f);
        return this;
    }

    public Collection<MaterialProperty> getProperties() {
        return properties.values();
    }
}
