package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.module.GameModule;
import com.github.rmheuer.engine.render2d.system.Render2DModule;

import java.util.Collection;
import java.util.Collections;

public final class GuiModule implements GameModule {
    @Override
    public Collection<Class<? extends GameModule>> getDependencies() {
        return Collections.singleton(Render2DModule.class);
    }

    @Override
    public void initializeWorld(Game.WorldBuilder builder) {
        builder.addSystem(GuiInputEventSystem.class);
        builder.addSystem(GuiProcessSystem.class);
    }
}
