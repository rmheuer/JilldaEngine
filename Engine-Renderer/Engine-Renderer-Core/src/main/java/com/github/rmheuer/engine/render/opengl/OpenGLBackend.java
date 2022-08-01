package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.Window;
import com.github.rmheuer.engine.render.WindowSettings;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;

import static org.lwjgl.opengl.GL33C.*;

public final class OpenGLBackend extends RenderBackend {
    @Override
    public void setViewportSize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void prepareFrame() {
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
    public Image.Native createImageNative(int width, int height) {
        return new GLImageNative(width, height);
    }

    @Override
    public CubeMap.Native createCubeMapNative() {
        return new GLCubeMapNative();
    }

    @Override
    public Shader.Native createShaderNative(ShaderType type, String source) {
        return new GLShaderNative(type, source);
    }

    @Override
    public ShaderProgram.Native createShaderProgramNative(ShaderProgram program, Shader.Native[] shaderNatives) {
        return new GLShaderProgramNative(program, shaderNatives);
    }

    @Override
    public Mesh.Native createMeshNative(PrimitiveType primType) {
        return new GLMeshNative(primType);
    }
}
