package com.github.rmheuer.engine.core.input.mouse;

public final class MouseButtonPressEvent extends MouseButtonEvent {
    public MouseButtonPressEvent(float x, float y, MouseButton button) {
	super(x, y, button);
    }

    @Override
    public String toString() {
	return "MouseButtonPressEvent{" +
	    "x=" + getX() + ", " +
	    "y=" + getY() + ", " +
	    "button=" + getButton() +
	    "}";
    }
}
