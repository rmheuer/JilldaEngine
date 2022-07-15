package com.github.rmheuer.engine.render.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.RenderContext;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.WindowSettings;
import com.github.rmheuer.engine.render.asset.CubeMapSerializer;
import com.github.rmheuer.engine.render.asset.ShaderProgramSerializer;
import com.github.rmheuer.engine.render.asset.ShaderSerializer;
import com.github.rmheuer.engine.render.asset.TextureDataSerializer;
import com.github.rmheuer.engine.render.asset.Texture2DSerializer;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.event.WindowCloseEvent;
import com.github.rmheuer.engine.render.event.WindowFramebufferResizeEvent;
import com.github.rmheuer.engine.render.event.WindowResizeEvent;
import com.github.rmheuer.engine.render.opengl.OpenGLBackend;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Texture2D;
import com.github.rmheuer.engine.render.texture.TextureData;

public final class RenderContextSystem implements GameSystem {
    private WindowSettings windowSettings;

    @Override
    public void init(World world) {
        if (windowSettings == null)
            windowSettings = new WindowSettings();

        RenderContext ctx = new RenderContext();
        world.setLocalSingleton(ctx);

        RenderBackend backend = new OpenGLBackend();
        RendererAPI.setBackend(backend);

        Game.get().getAssetManager().registerSerializer(ShaderProgram.class, new ShaderProgramSerializer());
        Game.get().getAssetManager().registerSerializer(Shader.class, new ShaderSerializer());
        Game.get().getAssetManager().registerSerializer(Texture2D.class, new Texture2DSerializer());
        Game.get().getAssetManager().registerSerializer(TextureData.class, new TextureDataSerializer());
        Game.get().getAssetManager().registerSerializer(CubeMap.class, new CubeMapSerializer());

        ctx.setWindow(backend.createWindow(windowSettings));
    }

    @Override
    public void onEvent(World world, EventDispatcher d) {
        d.dispatch(WindowCloseEvent.class, (event) -> Game.get().stop());
        d.dispatch(WindowFramebufferResizeEvent.class, (event) -> {
            RendererAPI.getBackend().setViewportSize(event.getWidth(), event.getHeight());
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
        ctx.getWindow().close();
    }
}
