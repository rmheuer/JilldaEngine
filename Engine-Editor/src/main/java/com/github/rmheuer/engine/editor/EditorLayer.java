package com.github.rmheuer.engine.editor;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.layer.WorldLayer;

import java.util.Arrays;
import java.util.Collection;

public final class EditorLayer extends WorldLayer {
    @Override
    protected Collection<Class<? extends GameSystem>> getAdditionalSystems() {
        return Arrays.asList(
                EditorSetupSceneSystem.class,
                EditorUpdateViewportSceneSystem.class
        );
    }
}
