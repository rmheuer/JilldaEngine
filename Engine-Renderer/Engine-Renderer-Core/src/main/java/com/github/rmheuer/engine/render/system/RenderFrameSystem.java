package com.github.rmheuer.engine.render.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.math.Transform;
import com.github.rmheuer.engine.render.RenderContext;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render.framebuffer.Framebuffer;

public final class RenderFrameSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
        RenderContext ctx = world.getLocalSingleton(RenderContext.class);

        boolean[] foundCamera = {false};
        world.forEach(Camera.class, Transform.class, (camera, tx) -> {
            foundCamera[0] = true;

            // If camera has no set framebuffer, it should use the default
            Framebuffer fb = camera.getFramebuffer();
            if (fb == null) {
                fb = ctx.getWindow().getDefaultFramebuffer();
            }

            fb.bind();
            RendererAPI.getBackend().clear();
            Game.get().postImmediateEvent(new RenderSceneEvent(camera, tx));
            fb.unbind();
        });

        // There is required to be at least one camera
        if (!foundCamera[0])
            throw new IllegalStateException("No camera in scene, or camera has no transform");

        ctx.getWindow().update();
    }
}
