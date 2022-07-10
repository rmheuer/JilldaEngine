package com.github.rmheuer.engine.core.input.mouse;

import com.github.rmheuer.engine.core.event.Event;

public abstract class MouseEvent implements Event {
    private final float x;
    private final float y;

    public MouseEvent(float x, float y) {
	this.x = x;
	this.y = y;
    }

    public float getX() {
	return x;
    }

    public float getY() {
	return y;
    }
}
