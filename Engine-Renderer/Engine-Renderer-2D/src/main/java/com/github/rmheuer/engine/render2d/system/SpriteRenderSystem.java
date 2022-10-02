package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.Before;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render2d.DrawList2D;
import com.github.rmheuer.engine.render2d.RenderContext2D;
import com.github.rmheuer.engine.render2d.component.SpriteRenderer;

public final class SpriteRenderSystem implements GameSystem {
    @OnEvent
    @After(RenderBeginFrame2DSystem.class)
    @Before(RenderEndFrame2DSystem.class)
    public void onRenderScene(World world, RenderSceneEvent event) {
        RenderContext2D ctx = world.getLocalSingleton(RenderContext2D.class);
        DrawList2D draw = ctx.getDrawList();

        world.forEach(SpriteRenderer.class, Transform.class, (spr, tx) -> {
            draw.applyTransform(tx);
            draw.drawImage(-0.5f, -0.5f, 1, 1, spr.getSprite(), spr.getTint(), 0, 0, 1, 1);
        });
    }
}
