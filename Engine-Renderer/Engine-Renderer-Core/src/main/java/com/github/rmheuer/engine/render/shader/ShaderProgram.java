package com.github.rmheuer.engine.render.shader;

import com.github.rmheuer.engine.core.asset.Asset;

public abstract class ShaderProgram extends Asset {
    public abstract void bind();
    public abstract void unbind();

    public abstract ShaderUniform getUniform(String name);

    public abstract Shader[] getShaders();
}
