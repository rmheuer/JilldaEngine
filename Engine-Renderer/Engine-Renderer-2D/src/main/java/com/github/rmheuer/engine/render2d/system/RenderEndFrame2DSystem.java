package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render2d.RenderContext2D;

@After(stage = Stage.EVENT, after = RenderBeginFrame2DSystem.class)
public final class RenderEndFrame2DSystem implements GameSystem {
    @Override
    public void onEvent(World world, EventDispatcher d) {
        d.dispatch(RenderSceneEvent.class, (e) -> {
            RenderContext2D ctx = world.getLocalSingleton(RenderContext2D.class);
            ctx.getRenderer().draw(ctx.getDrawList());
        });
    }
}
