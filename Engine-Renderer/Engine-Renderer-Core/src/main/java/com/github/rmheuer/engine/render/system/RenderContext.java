package com.github.rmheuer.engine.render.system;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;
import com.github.rmheuer.engine.render.camera.PrimaryCamera;

public final class RenderContext implements WorldLocalSingleton {
    private PrimaryCamera primaryCamera;

    public RenderContext() {
        primaryCamera = null;
    }

    public PrimaryCamera getPrimaryCamera() {
        return primaryCamera;
    }

    void setPrimaryCamera(PrimaryCamera primaryCamera) {
        this.primaryCamera = primaryCamera;
    }
}
