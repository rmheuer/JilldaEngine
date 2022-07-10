package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderUniform;

import static org.lwjgl.opengl.GL33C.*;

// TODO: Preprocess GLSL to find uniforms, attributes, and outputs
// TODO: Add #include to GLSL
public final class OpenGLShaderProgram implements ShaderProgram {
    private final int id;
    private final Shader[] shaders;

    public OpenGLShaderProgram(Shader... shaders) {
	id = glCreateProgram();
	this.shaders = shaders;

	for (Shader shader : shaders) {
	    OpenGLShader glShader = (OpenGLShader) shader;
	    glAttachShader(id, glShader.getId());
	}

	glLinkProgram(id);
	if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
	    System.err.println(glGetProgramInfoLog(id));
	    throw new RuntimeException("Shader program linking failed");
	}
    }

    @Override
    public void bind() {
	glUseProgram(id);
    }

    @Override
    public void unbind() {
	glUseProgram(0);
    }

    @Override
    public ShaderUniform getUniform(String name) {
	int location = glGetUniformLocation(id, name);
	return new OpenGLShaderUniform(name, location);
    }

    @Override
    public void delete() {
	for (Shader shader : shaders) {
	    shader.delete();
	}
	glDeleteProgram(id);
    }
}
