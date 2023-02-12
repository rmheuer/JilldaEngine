package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.module.GameModule;
import com.github.rmheuer.engine.render.system.RenderModule;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class Render2DModule implements GameModule {
    @Override
    public Collection<Class<? extends GameModule>> getDependencies() {
        return Collections.singleton(RenderModule.class);
    }

    @Override
    public void initializeWorld(Game.WorldBuilder builder) {
        builder
                .addSystem(RenderContext2DSystem.class)
                .addSystem(RenderBeginFrame2DSystem.class)
                .addSystem(RenderEndFrame2DSystem.class)
                .addSystem(RenderScreenSpace2DSystem.class)
                .addSystem(SpriteRenderSystem.class)
                .addSystem(AnimateSpriteSystem.class)
                .addSystem(Render2DPrepareCanvas2DSystem.class)
                .addSystem(RenderCanvas2DSystem.class);
    }
}
