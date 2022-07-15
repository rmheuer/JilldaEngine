package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.Window;
import com.github.rmheuer.engine.render.WindowSettings;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.mesh.Vertex;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.texture.Texture2D;
import com.github.rmheuer.engine.render.texture.TextureData;
import com.github.rmheuer.engine.render.texture.TextureSettings;

import java.io.IOException;

import static org.lwjgl.opengl.GL33C.*;

public final class OpenGLBackend implements RenderBackend {
    @Override
    public void setViewportSize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void clear() {
        glClearColor(0.1f, 0.2f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public Window createWindow(WindowSettings settings) {
        return new OpenGLWindow(settings);
    }

    @Override
    public Shader createShader(ResourceFile res) throws IOException {
        return new OpenGLShader(res);
    }

    @Override
    public ShaderProgram createShaderProgram(Shader... shaders) {
        return new OpenGLShaderProgram(shaders);
    }

    @Override
    public <V extends Vertex> Mesh<V> createMesh(PrimitiveType primType) {
        return new OpenGLMesh<>(primType);
    }

    @Override
    public Texture2D createTexture(ResourceFile res, TextureSettings settings) throws IOException {
        return new OpenGLTexture2D(res, settings);
    }

    @Override
    public Texture2D createTexture(TextureData data, TextureSettings settings) {
        return new OpenGLTexture2D(data, settings);
    }
}
