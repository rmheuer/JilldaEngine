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

    public Image getTexture2D(String key) {
        return getProperty(key).getTexture2D();
    }

    public CubeMap getCubeMap(String key) { return getProperty(key).getCubeMap(); }

    private MaterialProperty getOrCreateProperty(String key) {
        return properties.computeIfAbsent(key, MaterialProperty::new);
    }

    public Material setTexture2D(String key, Image texture) {
        getOrCreateProperty(key).setTexture2D(texture);
        return this;
    }

    public Material setCubeMap(String key, CubeMap map) {
        getOrCreateProperty(key).setCubeMap(map);
        return this;
    }

    public Collection<MaterialProperty> getProperties() {
        return properties.values();
    }
}
