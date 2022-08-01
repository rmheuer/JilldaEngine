package com.github.rmheuer.engine.render;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.core.serial.obj.Transient;

@Transient
public final class RenderContext implements WorldLocalSingleton {
    private final NativeObjectManager nativeObjectManager;
    private Window window;

    public RenderContext() {
        nativeObjectManager = new NativeObjectManager();
    }

    public Window getWindow() {
	return window;
    }

    public void setWindow(Window window) {
	this.window = window;
    }

    public NativeObjectManager getNativeObjectManager() {
        return nativeObjectManager;
    }
}
