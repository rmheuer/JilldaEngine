package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderType;

import java.io.IOException;

import static org.lwjgl.opengl.GL33C.*;

public final class OpenGLShader implements Shader {
    private final int id;

    public OpenGLShader(ResourceFile res, ShaderType type) throws IOException {
        int glType;
        if (type == ShaderType.VERTEX) {
            glType = GL_VERTEX_SHADER;
        } else {
            glType = GL_FRAGMENT_SHADER;
        }

        id = glCreateShader(glType);
        glShaderSource(id, res.readAsString());
        glCompileShader(id);
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(id));
            throw new RuntimeException("Shader compilation failed");
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public void delete() {
        glDeleteShader(id);
    }
}
