package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.gui.GuiRenderer;

@After(after = GuiRenderSystem.class)
public final class GuiInputEndFrameSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
	world.getLocalSingleton(GuiRenderer.class).getInput().endFrame();
    }
}
