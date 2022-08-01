package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;
import com.github.rmheuer.engine.render.RenderContext;
import com.github.rmheuer.engine.render.system.RenderContextSystem;
import com.github.rmheuer.engine.render2d.RenderContext2D;
import com.github.rmheuer.engine.render2d.Renderer2D;

@After(stage = Stage.INIT, after = RenderContextSystem.class)
public final class RenderContext2DSystem implements GameSystem {
    @Override
    public void init(World world) {
        RenderContext ctx = world.getLocalSingleton(RenderContext.class);

        RenderContext2D ctx2d = new RenderContext2D();
        world.setLocalSingleton(ctx2d);

        ctx2d.setRenderer(new Renderer2D(ctx.getNativeObjectManager()));
    }
}
