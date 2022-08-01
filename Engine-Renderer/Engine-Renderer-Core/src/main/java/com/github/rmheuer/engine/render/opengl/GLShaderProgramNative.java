package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderUniform;
import org.lwjgl.opengl.GL33C;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33C.*;

public final class GLShaderProgramNative implements ShaderProgram.Native {
    private final ShaderProgram program;
    private final int id;
    private final Map<String, Integer> uniformLocations;

    public GLShaderProgramNative(ShaderProgram program, Shader.Native[] shaderNatives) {
        this.program = program;

        id = glCreateProgram();
        for (Shader.Native shader : shaderNatives) {
            GLShaderNative gl = (GLShaderNative) shader;
            glAttachShader(id, gl.getId());
        }

        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println(glGetProgramInfoLog(id));
            throw new RuntimeException("Shader program linking failed");
        }

        uniformLocations = new HashMap<>();
    }

    @Override
    public void bind() {
        glUseProgram(id);
    }

    @Override
    public void unbind() {
        glUseProgram(0);
    }

    private int getUniformLocation(String name) {
        return uniformLocations.computeIfAbsent(name, (n) -> glGetUniformLocation(id, n));
    }

    private void updateUniform(ShaderUniform uniform) {
        int loc = getUniformLocation(uniform.getName());

        if (uniform.isFloat()) {
            glUniform1f(loc, uniform.getFloat());
        } else if (uniform.isVector2f()) {
            Vector2f v = uniform.getVector2f();
            glUniform2f(loc, v.x, v.y);
        } else if (uniform.isVector3f()) {
            Vector3f v = uniform.getVector3f();
            glUniform3f(loc, v.x, v.y, v.z);
        } else if (uniform.isVector4f()) {
            Vector4f v = uniform.getVector4f();
            glUniform4f(loc, v.x, v.y, v.z, v.w);
        } else if (uniform.isInt()) {
            glUniform1i(loc, uniform.getInt());
        } else if (uniform.isMatrix4f()) {
            glUniformMatrix4fv(loc, false, uniform.getMatrix4f().getColumnMajor());
        }
    }

    @Override
    public void updateUniformValues() {
        for (ShaderUniform uniform : program.getUniforms()) {
            if (uniform.isDirty()) {
                updateUniform(uniform);
                uniform.clearDirty();
            }
        }
    }

    @Override
    public NativeObjectFreeFn getFreeFn() {
        return new GLDestructor(id, GL33C::glDeleteProgram);
    }
}
