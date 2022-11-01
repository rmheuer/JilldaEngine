package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.render.BufferType;
import com.github.rmheuer.engine.render.DepthMode;
import com.github.rmheuer.engine.render.PolygonMode;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render2d.DrawList2D;
import com.github.rmheuer.engine.render2d.RenderContext2D;

public final class RenderBeginFrame2DSystem implements GameSystem {
    @OnEvent
    public void onRenderScene(World world, RenderSceneEvent e) {
        RenderContext2D ctx = world.getLocalSingleton(RenderContext2D.class);
        ctx.getRenderer().setView(e.getCamera().getProjection(), e.getCameraTransform());
        ctx.setDrawList(new DrawList2D());
        RenderBackend.get().clear(BufferType.DEPTH);
        RenderBackend.get().setDepthMode(DepthMode.TEST_ONLY); // 2d does not write to depth buffer
        RenderBackend.get().setPolygonMode(PolygonMode.FILL);
    }
}
