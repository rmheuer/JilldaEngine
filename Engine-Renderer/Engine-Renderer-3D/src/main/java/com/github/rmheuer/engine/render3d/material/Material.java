package com.github.rmheuer.engine.render3d.material;

import com.github.rmheuer.engine.core.asset.Asset;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.texture.Texture2D;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Material extends Asset {
    private final Map<String, MaterialProperty> properties;
    private ShaderProgram shader;

    public Material() {
        this(null);
    }

    public Material(ShaderProgram shader) {
        properties = new HashMap<>();
        this.shader = shader;
        if (shader != null)
            shader.claim();
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public Material setShader(ShaderProgram shader) {
        if (this.shader != null)
            this.shader.release();

        this.shader = shader;
        if (shader != null)
            shader.claim();

        return this;
    }

    public MaterialProperty getProperty(String key) {
        return properties.get(key);
    }

    public Texture2D getTexture2D(String key) {
        return getProperty(key).getTexture2D();
    }

    private MaterialProperty getOrCreateProperty(String key) {
        return properties.computeIfAbsent(key, MaterialProperty::new);
    }

    public Material setTexture2D(String key, Texture2D texture) {
        getOrCreateProperty(key).setTexture2D(texture);
        return this;
    }

    public Collection<MaterialProperty> getProperties() {
        return properties.values();
    }

    @Override
    protected void freeAsset() {
        if (shader != null)
            shader.release();
    }
}
