package com.github.rmheuer.engine.physics2d;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.module.GameModule;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class Physics2DModule implements GameModule {
    @Override
    public Collection<Class<? extends GameSystem>> getSystems() {
        return Collections.emptySet();
    }
}
