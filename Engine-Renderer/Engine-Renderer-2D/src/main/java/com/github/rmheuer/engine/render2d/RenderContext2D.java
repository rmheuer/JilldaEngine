package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;

public final class RenderContext2D implements WorldLocalSingleton {
    private Renderer2D renderer;

    public Renderer2D getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer2D renderer) {
        this.renderer = renderer;
    }
}
