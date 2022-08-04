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
    private final OpenGL gl;

    public OpenGLBackend(OpenGL gl) {
        this.gl = gl;
    }

    @Override
    public void setViewportSize(int width, int height) {
        gl.viewport(0, 0, width, height);
    }

    @Override
    public void prepareFrame() {
        gl.clearColor(0.0f, 0.15f, 0.3f, 1.0f);
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
        gl.enable(gl.DEPTH_TEST);
        gl.enable(gl.BLEND);
        gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public Window createWindow(WindowSettings settings) {
        return new OpenGLWindow(settings);
    }

    @Override
    public Image.Native createImageNative(int width, int height) {
        return new GLImageNative(gl, width, height);
    }

    @Override
    public CubeMap.Native createCubeMapNative() {
        return new GLCubeMapNative(gl);
    }

    @Override
    public Shader.Native createShaderNative(ShaderType type, String source) {
        return new GLShaderNative(gl, type, source);
    }

    @Override
    public ShaderProgram.Native createShaderProgramNative(ShaderProgram program, Shader.Native[] shaderNatives) {
        return new GLShaderProgramNative(gl, program, shaderNatives);
    }

    @Override
    public Mesh.Native createMeshNative(PrimitiveType primType) {
        return new GLMeshNative(gl, primType);
    }
}
