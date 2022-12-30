package com.github.rmheuer.engine.render3d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.Before;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.module.ModuleRegistry;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.BufferType;
import com.github.rmheuer.engine.render.DepthMode;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.RenderConstants;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.shader.ShaderConstants;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderUniform;
import com.github.rmheuer.engine.render.system.RenderModule;
import com.github.rmheuer.engine.render.texture.Texture;
import com.github.rmheuer.engine.render2d.system.RenderBeginFrame2DSystem;
import com.github.rmheuer.engine.render3d.component.MeshRenderer;
import com.github.rmheuer.engine.render3d.material.Material;
import com.github.rmheuer.engine.render3d.material.MaterialProperty;

public final class MeshRenderSystem implements GameSystem {
    @OnEvent
    @Before(RenderBeginFrame2DSystem.class)
    public void onRenderScene(World world, RenderSceneEvent event) {
        RenderBackend.get().clear(BufferType.DEPTH);
        RenderBackend.get().setDepthMode(DepthMode.TEST_AND_WRITE);
        RenderBackend.get().setStencilEnabled(false);

        world.forEach(MeshRenderer.class, Transform.class, (m, tx) -> {
            if (!m.isEnabled())
                return;

            Material mat = m.getMaterial();
            if (mat == null)
                return;

            ShaderProgram shader = mat.getShader();
            Mesh mesh = m.getMesh();
            if (mesh == null)
                return;

            if (!mesh.hasData())
                return;

            NativeObjectManager nom = ModuleRegistry.getInstance(RenderModule.class).getNativeObjectManager();
            ShaderProgram.Native nShader = shader.getNative(nom);
            Mesh.Native nMesh = mesh.getNative(nom);
            Texture.Native[] nTextures = new Texture.Native[RenderConstants.MAX_TEXTURE_SLOTS];

            int slotIdx = 0;
            nShader.bind();
            for (MaterialProperty prop : mat.getProperties()) {
                ShaderUniform u = shader.getUniform(prop.getName());

                if (prop.isTexture()) {
                    nTextures[slotIdx] = prop.getTexture().getNative(nom);
                    u.setInt(slotIdx);
                    slotIdx++;
                } else if (prop.isFloat()) {
                    u.setFloat(prop.getFloat());
                }
            }

            shader.getUniform(ShaderConstants.UNIFORM_NAME_PROJECTION).setMatrix4f(event.getCamera().getProjection().getMatrix());
            shader.getUniform(ShaderConstants.UNIFORM_NAME_VIEW).setMatrix4f(event.getCameraTransform().getGlobalInverseMatrix());
            shader.getUniform(ShaderConstants.UNIFORM_NAME_TRANSFORM).setMatrix4f(tx.getGlobalMatrix());

            for (int i = 0; i < nTextures.length; i++) {
                Texture.Native tex = nTextures[i];
                if (tex == null)
                    break;

                tex.bindToSlot(i);
            }

            nShader.updateUniformValues();
            RenderBackend.get().setCullMode(m.getWindingOrder(), m.getCullMode());
            RenderBackend.get().setPolygonMode(m.getPolygonMode());
            nMesh.render();
            nShader.unbind();
        });
    }
}
