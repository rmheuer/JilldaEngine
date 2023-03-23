package com.github.rmheuer.engine.render.vulkan;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;
import com.github.rmheuer.engine.render.*;
import com.github.rmheuer.engine.render.glfw.GLFWManager;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshData;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.TextureFilter;

public final class VulkanBackend extends RenderBackend {
    private static final boolean ENABLE_VALIDATION = true;

    private final VulkanContext context;

    public VulkanBackend() {
        // Require GLFW immediately; we need it to get the required
        // instance extensions
        GLFWManager.require();

        context = new VulkanContext(ENABLE_VALIDATION);
    }

    @Override
    public Window createWindow(WindowSettings settings) {
        return new VulkanWindow(context, settings);
    }

    @Override
    public void setViewportRect(int x, int y, int width, int height) {

    }

    @Override
    public void prepareFrame() {

    }

    @Override
    public void clear(BufferType... buffers) {

    }

    @Override
    public void setCullMode(WindingOrder windingOrder, CullMode mode) {

    }

    @Override
    public void setDepthMode(DepthMode mode) {

    }

    @Override
    public void setPolygonMode(PolygonMode mode) {

    }

    @Override
    public void setStencilEnabled(boolean enabled) {

    }

    @Override
    public void setStencilFunc(StencilFunc func, int reference, int mask) {

    }

    @Override
    public void setStencilOp(StencilOp stencilFail, StencilOp depthFail, StencilOp pass) {

    }

    @Override
    public Image.Native createImageNative(int width, int height) {
        return new Image.Native() {
            @Override
            public void setData(int[] rgbaData) {

            }

            @Override
            public void setFilters(TextureFilter minFilter, TextureFilter magFilter) {

            }

            @Override
            public void bindToSlot(int slot) {

            }

            @Override
            public NativeObjectFreeFn getFreeFn() {
                return () -> {};
            }
        };
    }

    @Override
    public CubeMap.Native createCubeMapNative() {
        return new CubeMap.Native() {
            @Override
            public void setPosXImage(Image posX) {

            }

            @Override
            public void setNegXImage(Image negX) {

            }

            @Override
            public void setPosYImage(Image posY) {

            }

            @Override
            public void setNegYImage(Image negY) {

            }

            @Override
            public void setPosZImage(Image posZ) {

            }

            @Override
            public void setNegZImage(Image negZ) {

            }

            @Override
            public void setFilters(TextureFilter minFilter, TextureFilter magFilter) {

            }

            @Override
            public void bindToSlot(int slot) {

            }

            @Override
            public NativeObjectFreeFn getFreeFn() {
                return () -> {};
            }
        };
    }

    @Override
    public Shader.Native createShaderNative(ShaderType type, String source) {
        return new Shader.Native() {
            @Override
            public NativeObjectFreeFn getFreeFn() {
                return () -> {};
            }
        };
    }

    @Override
    public ShaderProgram.Native createShaderProgramNative(ShaderProgram program, Shader.Native[] shaderNatives) {
        return new ShaderProgram.Native() {
            @Override
            public void bind() {

            }

            @Override
            public void unbind() {

            }

            @Override
            public void updateUniformValues() {

            }

            @Override
            public NativeObjectFreeFn getFreeFn() {
                return () -> {};
            }
        };
    }

    @Override
    public Mesh.Native createMeshNative(PrimitiveType primType) {
        return new Mesh.Native() {
            @Override
            public void setData(MeshData data, MeshDataUsage usage) {

            }

            @Override
            public void render() {

            }

            @Override
            public NativeObjectFreeFn getFreeFn() {
                return () -> {};
            }
        };
    }
}
