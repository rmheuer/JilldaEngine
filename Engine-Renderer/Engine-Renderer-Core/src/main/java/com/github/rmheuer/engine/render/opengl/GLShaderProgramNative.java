package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderUniform;

import java.util.HashMap;
import java.util.Map;

public final class GLShaderProgramNative implements ShaderProgram.Native {
    private final OpenGL gl;
    private final ShaderProgram program;
    private final int id;
    private final Map<String, Integer> uniformLocations;

    public GLShaderProgramNative(OpenGL gl, ShaderProgram program, Shader.Native[] shaderNatives) {
        this.gl = gl;
        this.program = program;

        id = gl.createProgram();
        for (Shader.Native shader : shaderNatives) {
            GLShaderNative glShader = (GLShaderNative) shader;
            gl.attachShader(id, glShader.getId());
        }

        gl.linkProgram(id);
        if (gl.getProgrami(id, gl.LINK_STATUS) == gl.FALSE) {
            System.err.println(gl.getProgramInfoLog(id));
            throw new RuntimeException("Shader program linking failed");
        }

        uniformLocations = new HashMap<>();
    }

    @Override
    public void bind() {
        gl.useProgram(id);
    }

    @Override
    public void unbind() {
        gl.useProgram(0);
    }

    private int getUniformLocation(String name) {
        return uniformLocations.computeIfAbsent(name, (n) -> gl.getUniformLocation(id, n));
    }

    private void updateUniform(ShaderUniform uniform) {
        int loc = getUniformLocation(uniform.getName());

        if (uniform.isFloat()) {
            gl.uniform1f(loc, uniform.getFloat());
        } else if (uniform.isVector2f()) {
            Vector2f v = uniform.getVector2f();
            gl.uniform2f(loc, v.x, v.y);
        } else if (uniform.isVector3f()) {
            Vector3f v = uniform.getVector3f();
            gl.uniform3f(loc, v.x, v.y, v.z);
        } else if (uniform.isVector4f()) {
            Vector4f v = uniform.getVector4f();
            gl.uniform4f(loc, v.x, v.y, v.z, v.w);
        } else if (uniform.isInt()) {
            gl.uniform1i(loc, uniform.getInt());
        } else if (uniform.isMatrix4f()) {
            gl.uniformMatrix4fv(loc, false, uniform.getMatrix4f().getColumnMajor());
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
        return new GLDestructor(id, gl::deleteProgram);
    }
}
