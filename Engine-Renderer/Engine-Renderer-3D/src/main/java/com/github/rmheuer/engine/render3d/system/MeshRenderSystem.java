package com.github.rmheuer.engine.render3d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.math.Transform;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.shader.ShaderConstants;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderUniform;
import com.github.rmheuer.engine.render.texture.Texture;
import com.github.rmheuer.engine.render3d.component.MeshRenderer;
import com.github.rmheuer.engine.render3d.material.Material;
import com.github.rmheuer.engine.render3d.material.MaterialProperty;

public final class MeshRenderSystem implements GameSystem {
    @Override
    public void onEvent(World world, EventDispatcher d) {
        d.dispatch(RenderSceneEvent.class, (event) -> {
            world.forEach(MeshRenderer.class, Transform.class, (m, tx) -> {
                Material mat = m.getMaterial();
                ShaderProgram shader = mat.getShader();
                Mesh<?> mesh = m.getMesh();

                Texture[] textures = new Texture[RendererAPI.getBackend().getMaxTextureSlots()];
                int slotIdx = 0;

                shader.bind();
                for (MaterialProperty prop : mat.getProperties()) {
                    ShaderUniform u = shader.getUniform(prop.getName());

                    if (prop.isTexture2D()) {
                        textures[slotIdx] = prop.getTexture2D();
                        u.setInt(slotIdx);
                        slotIdx++;
                    }
                }

                shader.getUniform(ShaderConstants.UNIFORM_NAME_PROJECTION).setMatrix4f(event.getCamera().getProjection().getMatrix());
                shader.getUniform(ShaderConstants.UNIFORM_NAME_VIEW).setMatrix4f(event.getCameraTransform().getInverseMatrix());
                shader.getUniform(ShaderConstants.UNIFORM_NAME_TRANSFORM).setMatrix4f(tx.getMatrix());

                for (int i = 0; i < textures.length; i++) {
                    Texture tex = textures[i];
                    if (tex == null)
                        break;

                    tex.bindToSlot(i);
                }

                mesh.draw();
                shader.unbind();
            });
        });
    }
}
