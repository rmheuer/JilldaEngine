package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.gui.GuiRenderer;

public final class GuiInputEventSystem implements GameSystem {
    @Override
    public void onEvent(World world, EventDispatcher d) {
	// Need to check if renderer is null in case an immediate event is fired
	// during initialization, when the renderer has not yet been set
	GuiRenderer r = world.getLocalSingleton(GuiRenderer.class);
	if (r != null)
	    r.getInput().onEvent(d);
    }
}
