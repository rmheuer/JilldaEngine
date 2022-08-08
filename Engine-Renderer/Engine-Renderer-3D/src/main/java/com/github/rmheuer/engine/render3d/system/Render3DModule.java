package com.github.rmheuer.engine.render3d.system;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.module.GameModule;

import java.util.Collection;
import java.util.Collections;

public final class Render3DModule implements GameModule {
    @Override
    public Collection<Class<? extends GameSystem>> getSystems() {
        return Collections.singletonList(MeshRenderSystem.class);
    }
}
