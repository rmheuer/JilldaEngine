package com.github.rmheuer.engine.core.input.mouse;

public abstract class MouseButtonEvent extends MouseEvent {
    private final MouseButton button;

    public MouseButtonEvent(float x, float y, MouseButton button) {
	super(x, y);
	this.button = button;
    }

    public MouseButton getButton() {
	return button;
    }
}
