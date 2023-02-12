package com.github.rmheuer.engine.core.module;

import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.transform.PropagateTransformSystem;

public final class CoreModule implements GameModule {
    @Override
    public void initializeWorld(Game.WorldBuilder builder) {
        builder.addSystem(PropagateTransformSystem.class);
    }
}
