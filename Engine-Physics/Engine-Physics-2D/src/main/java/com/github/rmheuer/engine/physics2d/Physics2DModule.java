package com.github.rmheuer.engine.physics2d;

import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.module.GameModule;
import com.github.rmheuer.engine.physics2d.system.Physics2DContextSystem;
import com.github.rmheuer.engine.physics2d.system.Physics2DCreateBodySystem;
import com.github.rmheuer.engine.physics2d.system.Physics2DStepSystem;

public final class Physics2DModule implements GameModule {
    @Override
    public void initializeWorld(Game.WorldBuilder builder) {
        builder.addSystem(Physics2DContextSystem.class);
        builder.addSystem(Physics2DCreateBodySystem.class);
        builder.addSystem(Physics2DStepSystem.class);
    }
}
