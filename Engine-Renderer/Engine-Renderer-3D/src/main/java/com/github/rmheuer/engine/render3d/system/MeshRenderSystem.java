package com.github.rmheuer.engine.render3d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.RenderConstants;
import com.github.rmheuer.engine.render.RenderContext;
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
            RenderContext ctx = world.getLocalSingleton(RenderContext.class);

            world.forEach(MeshRenderer.class, Transform.class, (m, tx) -> {
                Material mat = m.getMaterial();
                ShaderProgram shader = mat.getShader();
                Mesh<?> mesh = m.getMesh();

                NativeObjectManager nom = ctx.getNativeObjectManager();
                ShaderProgram.Native nShader = shader.getNative(nom);

                Texture[] textures = new Texture[RenderConstants.MAX_TEXTURE_SLOTS];
                int slotIdx = 0;

                nShader.bind();
                for (MaterialProperty prop : mat.getProperties()) {
                    ShaderUniform u = shader.getUniform(prop.getName());

                    if (prop.isTexture2D()) {
                        textures[slotIdx] = prop.getTexture2D();
                        u.setInt(slotIdx);
                        slotIdx++;
                    } else if (prop.isCubeMap()) {
                        textures[slotIdx] = prop.getCubeMap();
                        u.setInt(slotIdx);
                        slotIdx++;
                    }
                }

                shader.getUniform(ShaderConstants.UNIFORM_NAME_PROJECTION).setMatrix4f(event.getCamera().getProjection().getMatrix());
                shader.getUniform(ShaderConstants.UNIFORM_NAME_VIEW).setMatrix4f(event.getCameraTransform().getGlobalInverseMatrix());
                shader.getUniform(ShaderConstants.UNIFORM_NAME_TRANSFORM).setMatrix4f(tx.getGlobalMatrix());

                for (int i = 0; i < textures.length; i++) {
                    Texture tex = textures[i];
                    if (tex == null)
                        break;

                    tex.getNative(nom).bindToSlot(i);
                }

                nShader.updateUniformValues();
                mesh.getNative(nom).render();
                nShader.unbind();
            });
        });
    }
}
