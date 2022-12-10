package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.math.*;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.component.GuiCanvas;
import com.github.rmheuer.engine.render2d.component.Canvas2D;

public final class GuiRenderSystem implements GameSystem {
    // TODO: Combine with GuiProcessSystem
    @Override
    @After(GuiProcessSystem.class)
    public void update(World world, float delta) {
        world.forEach(GuiCanvas.class, Canvas2D.class, Transform.class, (canvas, canvas2d, transform) -> {
            GuiRenderer gui = canvas.getRenderer();

            canvas2d.getDrawList().join(gui.getDrawList());

            float x = gui.getInput().getCursorPos().x;
            float y = gui.getInput().getCursorPos().y;
            canvas2d.getDrawList().fillQuad(x - 5, y - 5, 10, 10, new Vector4f(1, 1, 0, 1));
        });
    }
}
