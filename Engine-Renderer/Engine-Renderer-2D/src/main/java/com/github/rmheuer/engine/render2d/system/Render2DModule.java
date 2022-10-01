package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.module.GameModule;

import java.util.Arrays;
import java.util.Collection;

public final class Render2DModule implements GameModule {
    @Override
    public Collection<Class<? extends GameSystem>> getSystems() {
        return Arrays.asList(
                RenderContext2DSystem.class,
                RenderBeginFrame2DSystem.class,
                RenderEndFrame2DSystem.class,
                SpriteRenderSystem.class
        );
    }
}
