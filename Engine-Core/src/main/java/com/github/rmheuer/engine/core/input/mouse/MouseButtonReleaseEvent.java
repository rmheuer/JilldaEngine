package com.github.rmheuer.engine.core.input.mouse;

public final class MouseButtonReleaseEvent extends MouseButtonEvent {
    public MouseButtonReleaseEvent(float x, float y, MouseButton button) {
	super(x, y, button);
    }

    @Override
    public String toString() {
	return "MouseButtonReleaseEvent{" +
	    "x=" + getX() + ", " +
	    "y=" + getY() + ", " +
	    "button=" + getButton() +
	    "}";
    }
}
