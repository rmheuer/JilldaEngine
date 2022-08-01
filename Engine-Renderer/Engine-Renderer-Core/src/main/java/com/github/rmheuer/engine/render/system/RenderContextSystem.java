package com.github.rmheuer.engine.render.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.RenderContext;
import com.github.rmheuer.engine.render.WindowSettings;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.event.WindowCloseEvent;
import com.github.rmheuer.engine.render.event.WindowFramebufferResizeEvent;
import com.github.rmheuer.engine.render.event.WindowResizeEvent;
import com.github.rmheuer.engine.render.opengl.OpenGLBackend;

public final class RenderContextSystem implements GameSystem {
    private WindowSettings windowSettings;

    @Override
    public void init(World world) {
        if (windowSettings == null)
            windowSettings = new WindowSettings();

        RenderContext ctx = new RenderContext();
        world.setLocalSingleton(ctx);

        // Will automatically set singleton instance
        // Might want to change later
        RenderBackend backend = new OpenGLBackend();

        ctx.setWindow(backend.createWindow(windowSettings));
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

    @Override
    public void close(World world) {
        RenderContext ctx = world.getLocalSingleton(RenderContext.class);
        ctx.getNativeObjectManager().freeAllObjects();
        ctx.getWindow().close();
    }
}
