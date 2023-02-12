package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.module.GameModule;

public final class GuiModule implements GameModule {
    @Override
    public void initializeWorld(Game.WorldBuilder builder) {
        builder.addSystem(GuiInputEventSystem.class);
        builder.addSystem(GuiProcessSystem.class);
    }
}
