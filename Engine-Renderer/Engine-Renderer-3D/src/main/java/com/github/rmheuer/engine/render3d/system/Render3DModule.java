package com.github.rmheuer.engine.render3d.system;

import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.module.GameModule;
import com.github.rmheuer.engine.render.system.RenderModule;

import java.util.Collection;
import java.util.Collections;

public final class Render3DModule implements GameModule {
    @Override
    public Collection<Class<? extends GameModule>> getDependencies() {
        return Collections.singleton(RenderModule.class);
    }

    @Override
    public void initializeWorld(Game.WorldBuilder builder) {
        builder.addSystem(MeshRenderSystem.class);
    }
}
