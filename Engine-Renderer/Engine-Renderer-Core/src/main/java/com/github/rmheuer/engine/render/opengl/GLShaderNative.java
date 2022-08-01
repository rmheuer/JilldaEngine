package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderType;

import static com.github.rmheuer.engine.render.opengl.GLEnumConversions.getGlShaderType;

public final class GLShaderNative implements Shader.Native {
    private final OpenGL gl;
    private final int id;

    public GLShaderNative(OpenGL gl, ShaderType type, String source) {
        this.gl = gl;
        id = gl.createShader(getGlShaderType(gl, type));
        gl.shaderSource(id, source);
        gl.compileShader(id);
        if (gl.getShaderi(id, gl.COMPILE_STATUS) == gl.FALSE) {
            System.err.println(gl.getShaderInfoLog(id));
            throw new RuntimeException("Shader compilation failed");
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public NativeObjectFreeFn getFreeFn() {
        return new GLDestructor(id, gl::deleteShader);
    }
}
