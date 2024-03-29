package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.render.*;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;

public final class OpenGLBackend extends RenderBackend {
    private final OpenGL gl;

    public OpenGLBackend(OpenGL gl) {
        this.gl = gl;
    }

    @Override
    public void setViewportRect(int x, int y, int width, int height) {
        gl.viewport(x, y, width, height);
    }

    @Override
    public void prepareFrame() {
        gl.clearColor(0.0f, 0.15f, 0.3f, 1.0f);
        gl.enable(gl.DEPTH_TEST);
        gl.enable(gl.BLEND);
        gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);
        gl.depthFunc(gl.LEQUAL);
    }

    @Override
    public void clear(BufferType... buffers) {
        int bits = 0;
        for (BufferType type : buffers) {
            bits |= GLEnumConversions.getGlBufferType(gl, type);
        }

        if (bits != 0)
            gl.clear(bits);
    }

    @Override
    public void setCullMode(WindingOrder windingOrder, CullMode mode) {
        gl.frontFace(GLEnumConversions.getGlWindingOrder(gl, windingOrder));
        if (mode == CullMode.NONE) {
            gl.disable(gl.CULL_FACE);
        } else {
            gl.enable(gl.CULL_FACE);
            gl.cullFace(GLEnumConversions.getGlCullMode(gl, mode));
        }
    }

    @Override
    public void setDepthMode(DepthMode mode) {
        switch (mode) {
            case DISABLED:
                gl.disable(gl.DEPTH_TEST);
                break;
            case TEST_ONLY:
                gl.enable(gl.DEPTH_TEST);
                gl.depthFunc(gl.LEQUAL);
                gl.depthMask(false);
                break;
            case WRITE_ONLY:
                gl.enable(gl.DEPTH_TEST);
                gl.depthFunc(gl.ALWAYS);
                gl.depthMask(true);
                break;
            case TEST_AND_WRITE:
                gl.enable(gl.DEPTH_TEST);
                gl.depthMask(true);
                gl.depthFunc(gl.LEQUAL);
                break;
        }
    }

    @Override
    public void setPolygonMode(PolygonMode mode) {
        if (mode == PolygonMode.FILL)
            gl.polygonMode(gl.FRONT_AND_BACK, gl.FILL);
        else
            gl.polygonMode(gl.FRONT_AND_BACK, gl.LINE);
    }

    @Override
    public void setStencilEnabled(boolean enabled) {
        if (enabled)
            gl.enable(gl.STENCIL_TEST);
        else
            gl.disable(gl.STENCIL_TEST);
    }

    @Override
    public void setStencilFunc(StencilFunc func, int reference, int mask) {
        gl.stencilFunc(GLEnumConversions.getGlStencilFunc(gl, func), reference, mask);
    }

    @Override
    public void setStencilOp(StencilOp stencilFail, StencilOp depthFail, StencilOp pass) {
        gl.stencilOp(
                GLEnumConversions.getGlStencilOp(gl, stencilFail),
                GLEnumConversions.getGlStencilOp(gl, depthFail),
                GLEnumConversions.getGlStencilOp(gl, pass)
        );
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
