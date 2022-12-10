package com.github.rmheuer.engine.render2d.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.render2d.DrawList2D;

public final class Canvas2D implements Component {
    private boolean isScreenSpace;
    private DrawList2D draw;

    public Canvas2D() {
        this(true);
    }

    public Canvas2D(boolean isScreenSpace) {
        this.isScreenSpace = isScreenSpace;
        draw = new DrawList2D();
    }

    public DrawList2D getDrawList() {
        return draw;
    }

    public void setDraw(DrawList2D draw) {
        this.draw = draw;
    }

    public boolean isScreenSpace() {
        return isScreenSpace;
    }

    public void setScreenSpace(boolean screenSpace) {
        isScreenSpace = screenSpace;
    }
}
