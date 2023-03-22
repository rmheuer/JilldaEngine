package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.render.system.RenderModule;
import com.github.rmheuer.engine.render2d.RenderContext2D;
import com.github.rmheuer.engine.render2d.Renderer2D;

public final class RenderContext2DSystem implements GameSystem {
    @Override
    public void init(World world) {
        RenderContext2D ctx2d = new RenderContext2D();
        world.setLocalSingleton(ctx2d);

        ctx2d.setRenderer(new Renderer2D(Game.get().getModule(RenderModule.class).getNativeObjectManager()));
    }
}
