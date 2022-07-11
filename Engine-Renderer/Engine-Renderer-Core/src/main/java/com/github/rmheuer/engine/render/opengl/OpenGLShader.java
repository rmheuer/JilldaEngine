package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderType;

import java.io.IOException;

import static org.lwjgl.opengl.GL33C.*;

public final class OpenGLShader extends Shader {
    private static final String TYPE_DIRECTIVE = "#type";
    private static final String TYPE_VERTEX = "vertex";
    private static final String TYPE_FRAGMENT = "fragment";

    private final int id;

    public OpenGLShader(ResourceFile res) throws IOException {
        String source = res.readAsString();
        String[] lines = source.replace("\r", "").split("\n");

        StringBuilder glSource = new StringBuilder();
        String type = null;
        for (String line : lines) {
            String[] tokens = line.split(" ");
            if (tokens.length == 0)
                continue;

            if (tokens[0].equals(TYPE_DIRECTIVE)) {
                if (tokens.length < 2)
                    throw new IOException("Invalid type directive");

                type = tokens[1];
            } else {
                glSource.append(line);
                glSource.append("\n");
            }
        }

        if (type == null)
            throw new IOException("No shader type specified");

        int glType;
        switch (type) {
            case TYPE_VERTEX:
                glType = GL_VERTEX_SHADER;
                break;
            case TYPE_FRAGMENT:
                glType = GL_FRAGMENT_SHADER;
                break;
            default:
                throw new IOException("Invalid shader type: " + type);
        }

        id = glCreateShader(glType);
        glShaderSource(id, glSource);
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
    public void freeAsset() {
        glDeleteShader(id);
    }
}
