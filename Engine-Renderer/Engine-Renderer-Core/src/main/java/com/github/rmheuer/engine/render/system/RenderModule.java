package com.github.rmheuer.engine.render.system;

import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.module.GameModule;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.render.BufferType;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.RenderConstants;
import com.github.rmheuer.engine.render.Window;
import com.github.rmheuer.engine.render.WindowSettings;
import com.github.rmheuer.engine.render.opengl.OpenGLBackend;
import com.github.rmheuer.engine.render.opengl.lwjgl.LwjglOpenGL;
import com.github.rmheuer.engine.render.vulkan.VulkanBackend;

public final class RenderModule implements GameModule {
    private NativeObjectManager nativeObjectManager;
    private Window window;

    @Override
    public void initializeWorld(Game.WorldBuilder builder) {
        builder.addSystem(RenderFrameSystem.class);
    }

    @Override
    public void init() {
        // Will automatically set singleton instance
        // Might want to change later
//        RenderBackend backend = new OpenGLBackend(new LwjglOpenGL());
        RenderBackend backend = new VulkanBackend();

        nativeObjectManager = new NativeObjectManager();
        window = backend.createWindow(new WindowSettings());
    }

    @Override
    public void preUpdate() {
        RenderBackend.get().prepareFrame();
        RenderBackend.get().clear(BufferType.COLOR, BufferType.DEPTH);
    }

    @Override
    public void postUpdate() {
        nativeObjectManager.freeUnusedObjects(RenderConstants.MAX_OBJECTS_FREED_PER_FRAME);
        window.update();
    }

    @Override
    public void close() {
        nativeObjectManager.freeAllObjects();
        window.close();
    }

    public Window getWindow() {
        return window;
    }

    public NativeObjectManager getNativeObjectManager() {
        return nativeObjectManager;
    }
}
