package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.InitializationGroup;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.module.ModuleRegistry;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.system.RenderModule;
import com.github.rmheuer.engine.render2d.DrawList2D;
import com.github.rmheuer.engine.render2d.component.Canvas2D;

public final class Render2DPrepareCanvas2DSystem implements GameSystem {
    @Override
    @RunInGroup(InitializationGroup.class)
    public void update(World world, float delta) {
        Vector2i size = ModuleRegistry.getInstance(RenderModule.class).getWindow().getSize();
        world.forEach(Canvas2D.class, Transform.class, (canvas, transform) -> {
            canvas.setDraw(new DrawList2D());
            if (canvas.isScreenSpace()) {
                Vector3f scale = transform.getScale();
                canvas.setScreenSpaceSize(new Vector2f(size.x / scale.x, size.y / scale.y));
            }
        });
    }
}
