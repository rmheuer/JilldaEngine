package com.github.rmheuer.sandbox.test3d;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.layer.WorldLayer;

import java.util.Arrays;
import java.util.Collection;

public final class SandboxLayer extends WorldLayer {
    @Override
    protected Collection<Class<? extends GameSystem>> getAdditionalSystems() {
        return Arrays.asList(
                SandboxInitSystem.class
        );
    }
}
