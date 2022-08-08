package com.github.rmheuer.engine.render.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.transform.PropagateTransformSystem;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render.event.WindowCloseEvent;
import com.github.rmheuer.engine.render.event.WindowFramebufferResizeEvent;
import com.github.rmheuer.engine.render.event.WindowResizeEvent;

@After(after = PropagateTransformSystem.class)
public final class RenderFrameSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
        boolean[] foundCamera = {false};
        world.forEach(Camera.class, Transform.class, (camera, tx) -> {
            foundCamera[0] = true;

            world.postEvent(new RenderSceneEvent(camera, tx));
        });

        // There is required to be at least one camera
        if (!foundCamera[0])
            throw new IllegalStateException("No camera in scene, or camera has no transform");
    }

    @Override
    public void onEvent(World world, EventDispatcher d) {
        d.dispatch(WindowCloseEvent.class, (event) -> Game.get().stop());
        d.dispatch(WindowFramebufferResizeEvent.class, (event) -> {
            RenderBackend.get().setViewportSize(event.getWidth(), event.getHeight());
        });
        d.dispatch(WindowResizeEvent.class, (event) -> {
            world.forEach(Camera.class, (camera) -> {
                camera.getProjection().resize(event.getWidth(), event.getHeight());
            });
        });
    }
}
