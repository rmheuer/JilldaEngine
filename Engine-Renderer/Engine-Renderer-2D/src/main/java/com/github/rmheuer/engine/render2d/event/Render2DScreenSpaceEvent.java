package com.github.rmheuer.engine.render2d.event;

import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.render2d.DrawList2D;

public final class Render2DScreenSpaceEvent implements Event {
    private final DrawList2D drawList;

    public Render2DScreenSpaceEvent() {
        drawList = new DrawList2D();
    }

    public DrawList2D getDrawList() {
        return drawList;
    }

    @Override
    public String toString() {
        return "Render2DScreenSpaceEvent{}";
    }
}
