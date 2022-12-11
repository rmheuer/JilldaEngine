package com.github.rmheuer.engine.render.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.PresentationGroup;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.PrimaryCamera;
import com.github.rmheuer.engine.render.event.*;

public final class RenderFrameSystem implements GameSystem {
    @Override
    public void init(World world) {
        world.setLocalSingleton(new RenderContext());
    }

    @Override
    @RunInGroup(PresentationGroup.class)
    public void update(World world, float delta) {
        RenderContext ctx = world.getLocalSingleton(RenderContext.class);

        world.forEach(Camera.class, Transform.class, (camera, tx) -> {
            ctx.setPrimaryCamera(new PrimaryCamera(camera, tx));

            world.postImmediateEvent(new RenderSceneEvent(camera, tx));
        });
    }

    @OnEvent
    public void onWindowClose(World world, WindowCloseEvent event) {
        Game.get().stop();
    }

    @OnEvent
    public void onWindowFramebufferResize(World world, WindowFramebufferResizeEvent event) {
        RenderBackend.get().setViewportSize(event.getWidth(), event.getHeight());
    }

    @OnEvent
    public void onWindowResize(World world, WindowResizeEvent event) {
        world.forEach(Camera.class, (camera) -> {
            camera.getProjection().resize(event.getWidth(), event.getHeight());
        });
    }
}
