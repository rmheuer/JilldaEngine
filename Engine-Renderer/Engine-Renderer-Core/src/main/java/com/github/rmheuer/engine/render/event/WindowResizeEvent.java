package com.github.rmheuer.engine.render.event;

import com.github.rmheuer.engine.render.Window;

public final class WindowResizeEvent extends WindowEvent {
    private final int width;
    private final int height;

    public WindowResizeEvent(Window window, int width, int height) {
	super(window);
	this.width = width;
	this.height = height;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    @Override
    public String toString() {
	return "WindowResizeEvent{" +
	    "window=" + getWindow() + ", " +
	    "width=" + width + ", " +
	    "height=" + height +
	    "}";
    }
}
