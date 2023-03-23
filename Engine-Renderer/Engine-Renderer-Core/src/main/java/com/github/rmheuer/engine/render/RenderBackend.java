package com.github.rmheuer.engine.render;

import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;

// FIXME: Should have a close method
public abstract class RenderBackend {
    private static RenderBackend instance;

    public static RenderBackend get() {
        return instance;
    }

    protected RenderBackend() {
        if (instance != null)
            throw new IllegalStateException("Render backend already instantiated");

        instance = this;
    }

    /**
     * Sets the viewport boundary rectangle for rendering.
     *
     * @param x x coordinate of the bottom-left corner in framebuffer pixel space, relative to the bottom left corner
     * @param y y coordinate of the bottom-left corner in framebuffer pixel space, relative to the bottom left corner
     * @param width width of the rectangle in framebuffer pixels
     * @param height height of the rectangle in framebuffer pixels
     */
    public abstract void setViewportRect(int x, int y, int width, int height);
    public abstract void prepareFrame();

    public abstract void clear(BufferType... buffers);
    public abstract void setCullMode(WindingOrder windingOrder, CullMode mode);
    public abstract void setDepthMode(DepthMode mode);
    public abstract void setPolygonMode(PolygonMode mode);

    public abstract void setStencilEnabled(boolean enabled);
    public abstract void setStencilFunc(StencilFunc func, int reference, int mask);
    public abstract void setStencilOp(StencilOp stencilFail, StencilOp depthFail, StencilOp pass);

    public abstract Window createWindow(WindowSettings settings);

    public abstract Image.Native createImageNative(int width, int height);
    public abstract CubeMap.Native createCubeMapNative();
    public abstract Shader.Native createShaderNative(ShaderType type, String source);
    public abstract ShaderProgram.Native createShaderProgramNative(ShaderProgram program, Shader.Native[] shaderNatives);
    public abstract Mesh.Native createMeshNative(PrimitiveType primType);
}
