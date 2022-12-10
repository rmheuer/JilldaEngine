package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.PresentationGroup;
import com.github.rmheuer.engine.render.system.RenderFrameSystem;
import com.github.rmheuer.engine.render2d.DrawList2D;
import com.github.rmheuer.engine.render2d.component.Canvas2D;

public final class Render2DClearCanvas2DSystem implements GameSystem {
    @Override
    @RunInGroup(PresentationGroup.class)
    @After(RenderFrameSystem.class)
    @After(RenderScreenSpace2DSystem.class)
    public void update(World world, float delta) {
        world.forEach(Canvas2D.class, (canvas) -> {
            canvas.setDraw(new DrawList2D());
        });
    }
}
