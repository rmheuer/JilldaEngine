package com.github.rmheuer.engine.physics2d;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.module.GameModule;
import com.github.rmheuer.engine.physics2d.system.Physics2DContextSystem;
import com.github.rmheuer.engine.physics2d.system.Physics2DCreateBodySystem;
import com.github.rmheuer.engine.physics2d.system.Physics2DStepSystem;

import java.util.Arrays;
import java.util.Collection;

public final class Physics2DModule implements GameModule {
    @Override
    public Collection<Class<? extends GameSystem>> getSystems() {
        return Arrays.asList(
                Physics2DContextSystem.class,
                Physics2DCreateBodySystem.class,
                Physics2DStepSystem.class
        );
    }
}
