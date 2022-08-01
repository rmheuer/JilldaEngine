package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderType;
import org.lwjgl.opengl.GL33C;

import static com.github.rmheuer.engine.render.opengl.GLEnumConversions.getGlShaderType;
import static org.lwjgl.opengl.GL33C.*;

public final class GLShaderNative implements Shader.Native {
    private final int id;

    public GLShaderNative(ShaderType type, String source) {
        id = glCreateShader(getGlShaderType(type));
        glShaderSource(id, source);
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
    public NativeObjectFreeFn getFreeFn() {
        return new GLDestructor(id, GL33C::glDeleteShader);
    }
}
