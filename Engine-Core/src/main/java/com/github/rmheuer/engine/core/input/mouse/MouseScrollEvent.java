package com.github.rmheuer.engine.core.input.mouse;

public final class MouseScrollEvent extends MouseEvent {
    private final float scrollX;
    private final float scrollY;
    private final float scrollPixelsX;
    private final float scrollPixelsY;

    public MouseScrollEvent(float x, float y, float scrollX, float scrollY, float scrollPixelsX, float scrollPixelsY) {
	super(x, y);
	this.scrollX = scrollX;
	this.scrollY = scrollY;
	this.scrollPixelsX = scrollPixelsX;
	this.scrollPixelsY = scrollPixelsY;
    }

    public float getScrollX() {
	return scrollX;
    }

    public float getScrollY() {
	return scrollY;
    }

    public float getScrollPixelsX() {
	return scrollPixelsX;
    }

    public float getScrollPixelsY() {
	return scrollPixelsY;
    }

    @Override
    public String toString() {
	return "MouseScrollEvent{" +
	    "x=" + getX() + ", " +
	    "y=" + getY() + ", " +
	    "scrollX=" + getScrollX() + ", " +
	    "scrollY=" + getScrollY() + ", " +
	    "scrollPixelsX=" + getScrollPixelsX() + ", " +
	    "scrollPixelsY=" + getScrollPixelsY() +
	    "}";
    }
}
