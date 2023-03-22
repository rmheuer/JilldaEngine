package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.PresentationGroup;
import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.core.module.ModuleRegistry;
import com.github.rmheuer.engine.render.DepthMode;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.Window;
import com.github.rmheuer.engine.render.system.RenderFrameSystem;
import com.github.rmheuer.engine.render.system.RenderModule;
import com.github.rmheuer.engine.render2d.DrawList2D;
import com.github.rmheuer.engine.render2d.RenderContext2D;
import com.github.rmheuer.engine.render2d.Renderer2D;
import com.github.rmheuer.engine.render2d.event.Render2DScreenSpaceEvent;

public final class RenderScreenSpace2DSystem implements GameSystem {
    @Override
    @RunInGroup(PresentationGroup.class)
    @After(RenderFrameSystem.class)
    public void update(World world, float delta) {
        Render2DScreenSpaceEvent event = new Render2DScreenSpaceEvent();
        world.postImmediateEvent(event);
        DrawList2D draw = event.getDrawList();
        if (draw.isEmpty())
            return;

        RenderContext2D ctx2d = world.getLocalSingleton(RenderContext2D.class);
        Renderer2D renderer = ctx2d.getRenderer();

        Window window = ModuleRegistry.getInstance(RenderModule.class).getWindow();
        Vector2i size = window.getSize();

        Matrix4f projection = new Matrix4f().ortho(0, size.x, size.y, 0, 1000, -1000);

        RenderBackend.get().setDepthMode(DepthMode.DISABLED);
        RenderBackend.get().setStencilEnabled(false);
        Vector2i fbSize = window.getFramebufferSize();
        RenderBackend.get().setViewportRect(0, 0, fbSize.x, fbSize.y);
        renderer.setView(projection, new Matrix4f());
        renderer.draw(draw);
    }
}
