package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render2d.RenderContext2D;

public final class RenderEndFrame2DSystem implements GameSystem {
    @OnEvent
    @After(Render2DGroup.class)
    public void onRenderScene(World world, RenderSceneEvent e) {
        RenderContext2D ctx = world.getLocalSingleton(RenderContext2D.class);
        ctx.getRenderer().draw(ctx.getDrawList());
    }
}
