package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.module.GameModule;

import java.util.Arrays;
import java.util.Collection;

public final class GuiModule implements GameModule {
    @Override
    public Collection<Class<? extends GameSystem>> getSystems() {
        return Arrays.asList(
                GuiInputEventSystem.class,
                GuiInputEndFrameSystem.class,
                GuiLayoutPersistenceSystem.class,
                GuiRenderSystem.class
        );
    }
}
