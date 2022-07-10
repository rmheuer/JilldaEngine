package com.github.rmheuer.engine.render.glfw;

import com.github.rmheuer.engine.core.input.mouse.Mouse;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.util.BiMap;

import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public final class GLFWMouse implements Mouse {
    private static final BiMap<MouseButton, Integer> buttonIds = new BiMap<>();
    static {
	buttonIds.put(MouseButton.LEFT, GLFW_MOUSE_BUTTON_LEFT);
	buttonIds.put(MouseButton.RIGHT, GLFW_MOUSE_BUTTON_RIGHT);
	buttonIds.put(MouseButton.MIDDLE, GLFW_MOUSE_BUTTON_MIDDLE);
    }

    public static int getGlfwId(MouseButton button) {
	return buttonIds.getBOrDefault(button, GLFW_MOUSE_BUTTON_LEFT);
    }

    public static MouseButton getMouseButton(int id) {
	return buttonIds.getAOrDefault(id, MouseButton.UNKNOWN);
    }

    private final long window;

    public GLFWMouse(long window) {
	this.window = window;
    }

    @Override
    public boolean isButtonPressed(MouseButton button) {
	int glfwButton = getGlfwId(button);
	return glfwGetMouseButton(window, glfwButton) == GLFW_PRESS;
    }

    @Override
    public Vector2f getCursorPos() {
	try (MemoryStack stack = MemoryStack.stackPush()) {
	    DoubleBuffer x = stack.mallocDouble(1);
	    DoubleBuffer y = stack.mallocDouble(1);
	    glfwGetCursorPos(window, x, y);
	    return new Vector2f((float) x.get(0), (float) y.get(0));
	}
    }
}
