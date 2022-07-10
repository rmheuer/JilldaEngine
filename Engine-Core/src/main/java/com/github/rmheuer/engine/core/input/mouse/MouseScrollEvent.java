package com.github.rmheuer.engine.core.input.mouse;

public final class MouseScrollEvent extends MouseEvent {
    private final float scrollX;
    private final float scrollY;

    public MouseScrollEvent(float x, float y, float scrollX, float scrollY) {
	super(x, y);
	this.scrollX = scrollX;
	this.scrollY = scrollY;
    }

    public float getScrollX() {
	return scrollX;
    }

    public float getScrollY() {
	return scrollY;
    }

    @Override
    public String toString() {
	return "MouseScrollEvent{" +
	    "x=" + getX() + ", " +
	    "y=" + getY() + ", " +
	    "scrollX=" + getScrollX() + ", " +
	    "scrollY=" + getScrollY() +
	    "}";
    }
}
