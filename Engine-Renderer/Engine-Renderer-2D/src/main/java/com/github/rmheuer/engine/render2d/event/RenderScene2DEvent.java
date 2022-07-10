package com.github.rmheuer.engine.render2d.event;

import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.render2d.Renderer2D;

public final class RenderScene2DEvent implements Event {
    private final Renderer2D renderer;

    public RenderScene2DEvent(Renderer2D renderer) {
        this.renderer = renderer;
    }

    public Renderer2D getRenderer() {
        return renderer;
    }

    @Override
    public String toString() {
        return "RenderScene2DEvent{" +
                "renderer=" + renderer +
                '}';
    }
}
