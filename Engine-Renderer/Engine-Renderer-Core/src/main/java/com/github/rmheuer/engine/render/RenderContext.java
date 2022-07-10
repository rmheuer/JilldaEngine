package com.github.rmheuer.engine.render;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;
import com.github.rmheuer.engine.core.serial.obj.Transient;

@Transient
public final class RenderContext implements WorldLocalSingleton {
    private RenderBackend backend;
    private Window window;

    public RenderContext() {}

    public RenderBackend getBackend() {
	return backend;
    }

    public void setBackend(RenderBackend backend) {
	this.backend = backend;
    }

    public Window getWindow() {
	return window;
    }

    public void setWindow(Window window) {
	this.window = window;
    }
}
