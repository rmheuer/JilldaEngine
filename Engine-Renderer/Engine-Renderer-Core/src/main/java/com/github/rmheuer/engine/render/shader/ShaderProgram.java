package com.github.rmheuer.engine.render.shader;

public interface ShaderProgram {
    void bind();
    void unbind();

    ShaderUniform getUniform(String name);
    
    void delete();
}
