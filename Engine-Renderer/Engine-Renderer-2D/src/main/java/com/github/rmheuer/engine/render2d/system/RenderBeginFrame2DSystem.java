package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.Before;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.render.BufferType;
import com.github.rmheuer.engine.render.DepthMode;
import com.github.rmheuer.engine.render.PolygonMode;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render2d.DrawList2D;
import com.github.rmheuer.engine.render2d.RenderContext2D;
import com.github.rmheuer.engine.render2d.component.Canvas2D;

public final class RenderBeginFrame2DSystem implements GameSystem {
    @OnEvent
    @Before(Render2DGroup.class)
    public void onRenderScene(World world, RenderSceneEvent e) {
        RenderContext2D ctx = world.getLocalSingleton(RenderContext2D.class);
        ctx.getRenderer().setView(
                e.getCamera().getProjection().getMatrix(),
                e.getCameraTransform().getGlobalInverseMatrix()
        );
        ctx.setDrawList(new DrawList2D());
        RenderBackend.get().setDepthMode(DepthMode.TEST_AND_WRITE);
        RenderBackend.get().setPolygonMode(PolygonMode.FILL);
        RenderBackend.get().setStencilEnabled(false);
    }
}
