package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;

// TODO: Remove this system, it is unneccesary
public final class GuiInputEndFrameSystem implements GameSystem {
    @Override
    @After(GuiRenderSystem.class)
    public void update(World world, float delta) {
	//world.getLocalSingleton(GuiRenderer.class).getInput().endFrame();
    }
}
