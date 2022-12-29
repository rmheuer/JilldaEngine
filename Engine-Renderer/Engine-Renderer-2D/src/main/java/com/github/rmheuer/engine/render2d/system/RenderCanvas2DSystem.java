package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.util.Pair;
import com.github.rmheuer.engine.render.*;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render2d.DrawList2D;
import com.github.rmheuer.engine.render2d.RenderContext2D;
import com.github.rmheuer.engine.render2d.Renderer2D;
import com.github.rmheuer.engine.render2d.component.Canvas2D;
import com.github.rmheuer.engine.render2d.event.Render2DScreenSpaceEvent;

import java.util.ArrayList;
import java.util.List;

public final class RenderCanvas2DSystem implements GameSystem {
    // Screen-space canvases
    @OnEvent
    public void onRenderScreenSpace(World world, Render2DScreenSpaceEvent event) {
        world.forEach(Canvas2D.class, Transform.class, (canvas2D, transform) -> {
            if (!canvas2D.isScreenSpace())
                return;

            DrawList2D draw = event.getDrawList();
            draw.join(canvas2D.getDrawList(), transform);
        });
    }

    // World-space canvases
    @OnEvent
    @After(RenderEndFrame2DSystem.class)
    public void onRenderScene(World world, RenderSceneEvent event) {
        // We need to collect the canvases first to guarantee that iteration
        // order is consistent, so the IDs are the same
        List<Pair<Canvas2D, Transform>> canvasesToDraw = new ArrayList<>();
        world.forEach(Canvas2D.class, Transform.class, (canvas, transform) -> {
            if (!canvas.isScreenSpace())
                canvasesToDraw.add(new Pair<>(canvas, transform));
        });

        // If there aren't any canvases to draw, exit early
        if (canvasesToDraw.isEmpty())
            return;

        RenderBackend backend = RenderBackend.get();
        Renderer2D renderer = world.getLocalSingleton(RenderContext2D.class).getRenderer();

        // Enable and clear the stencil buffer
        backend.setStencilEnabled(true);
        backend.clear(BufferType.STENCIL);

        // Draw each canvas to the depth and stencil buffer to make a mask set
        // ID starts at 1 since the stencil buffer is cleared to zero
        backend.setDepthMode(DepthMode.TEST_AND_WRITE);
        backend.setStencilOp(StencilOp.KEEP, StencilOp.KEEP, StencilOp.REPLACE);
        int canvasId = 1;
        for (Pair<Canvas2D, Transform> pair : canvasesToDraw) {
            Canvas2D canvas = pair.getA();
            Transform transform = pair.getB();

            backend.setStencilFunc(StencilFunc.ALWAYS, canvasId++, 0xFF);
            renderer.draw(canvas.getDrawList(), transform);
        }

        // Draw each canvas again, masked by the stencil test
        backend.setDepthMode(DepthMode.DISABLED);
        backend.setStencilOp(StencilOp.KEEP, StencilOp.KEEP, StencilOp.KEEP);
        canvasId = 1;
        for (Pair<Canvas2D, Transform> pair : canvasesToDraw) {
            Canvas2D canvas = pair.getA();
            Transform transform = pair.getB();

            backend.setStencilFunc(StencilFunc.EQUAL, canvasId++, 0xFF);
            renderer.draw(canvas.getDrawList(), transform);
        }
    }
}
