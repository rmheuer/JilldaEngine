package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.core.input.keyboard.CharTypeEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyPressEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyReleaseEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyRepeatEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseButtonPressEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseButtonReleaseEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseScrollEvent;
import com.github.rmheuer.engine.gui.component.GuiCanvas;

public final class GuiInputEventSystem implements GameSystem {
    @OnEvent
    public void onMouseButtonPress(World world, MouseButtonPressEvent event) {
        world.forEach(GuiCanvas.class, (canvas) -> {
            canvas.getRenderer().getInput().mouseButtonPressed(event.getButton());
        });
    }

    @OnEvent
    public void onMouseButtonRelease(World world, MouseButtonReleaseEvent event) {
        world.forEach(GuiCanvas.class, (canvas) -> {
            canvas.getRenderer().getInput().mouseButtonReleased(event.getButton());
        });
    }

    @OnEvent
    public void onMouseScroll(World world, MouseScrollEvent event) {
        world.forEach(GuiCanvas.class, (canvas) -> {
            canvas.getRenderer().getInput().mouseScrolled(event.getScrollPixelsX(), event.getScrollPixelsY());
        });
    }

    @OnEvent
    public void onKeyPress(World world, KeyPressEvent event) {
        world.forEach(GuiCanvas.class, (canvas) -> {
            canvas.getRenderer().getInput().keyPressedOrRepeated(event.getKey());
        });
    }

    @OnEvent
    public void onKeyRepeat(World world, KeyRepeatEvent event) {
        world.forEach(GuiCanvas.class, (canvas) -> {
            canvas.getRenderer().getInput().keyPressedOrRepeated(event.getKey());
        });
    }

    @OnEvent
    public void onKeyRelease(World world, KeyReleaseEvent event) {
        world.forEach(GuiCanvas.class, (canvas) -> {
            canvas.getRenderer().getInput().keyReleased(event.getKey());
        });
    }

    @OnEvent
    public void onCharType(World world, CharTypeEvent event) {
        world.forEach(GuiCanvas.class, (canvas) -> {
            canvas.getRenderer().getInput().charTyped(event.getChar());
        });
    }
}
