package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;

public final class RenderContext2D implements WorldLocalSingleton {
    private Renderer2D renderer;
    private DrawList2D drawList;

    public Renderer2D getRenderer() {
        return renderer;
    }

    public DrawList2D getDrawList() {
        return drawList;
    }

    public void setRenderer(Renderer2D renderer) {
        this.renderer = renderer;
    }

    public void setDrawList(DrawList2D drawList) {
        this.drawList = drawList;
    }
}
