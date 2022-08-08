package com.github.rmheuer.engine.core.module;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.transform.PropagateTransformSystem;

import java.util.Collection;
import java.util.Collections;

public final class CoreModule implements GameModule {
    @Override
    public Collection<Class<? extends GameSystem>> getSystems() {
        return Collections.singletonList(PropagateTransformSystem.class);
    }
}
