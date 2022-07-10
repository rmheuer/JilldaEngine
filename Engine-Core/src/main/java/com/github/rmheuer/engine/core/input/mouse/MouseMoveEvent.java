package com.github.rmheuer.engine.core.input.mouse;

public final class MouseMoveEvent extends MouseEvent {
    public MouseMoveEvent(float x, float y) {
	super(x, y);
    }

    @Override
    public String toString() {
	return "MouseMoveEvent{" +
	    "x=" + getX() + ", " +
	    "y=" + getY() +
	    "}";
    }
}
