package com.github.rmheuer.engine.render;

import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;

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

    public abstract void setViewportSize(int width, int height);
    public abstract void prepareFrame();

    public abstract void clear(BufferType... buffers);
    public abstract void setCullMode(WindingOrder windingOrder, CullMode mode);
    public abstract void setDepthMode(DepthMode mode);

    public abstract Window createWindow(WindowSettings settings);

    public abstract Image.Native createImageNative(int width, int height);
    public abstract CubeMap.Native createCubeMapNative();
    public abstract Shader.Native createShaderNative(ShaderType type, String source);
    public abstract ShaderProgram.Native createShaderProgramNative(ShaderProgram program, Shader.Native[] shaderNatives);
    public abstract Mesh.Native createMeshNative(PrimitiveType primType);
}
