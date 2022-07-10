package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.render.shader.ShaderUniform;

import static org.lwjgl.opengl.GL33C.*;

// TODO: Ensure program is bound before setting
public final class OpenGLShaderUniform implements ShaderUniform {
    private final String name;
    private final int loc;

    public OpenGLShaderUniform(String name, int loc) {
	this.name = name;
	this.loc = loc;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public void setFloat(float f) {
	glUniform1f(loc, f);
    }

    @Override
    public void setVector2f(float x, float y) {
	glUniform2f(loc, x, y);
    }

    @Override
    public void setVector3f(float x, float y, float z) {
	glUniform3f(loc, x, y, z);
    }

    @Override
    public void setVector4f(float x, float y, float z, float w) {
	glUniform4f(loc, x, y, z, w);
    }

    @Override
    public void setMatrix4f(Matrix4f mat) {
	float[] data = mat.getColumnMajor();
	glUniformMatrix4fv(loc, false, data);
    }

    @Override
    public void setInt(int i) {
	glUniform1i(loc, i);
    }
}
