package com.github.rmheuer.engine.render.glfw;

import com.github.rmheuer.engine.core.input.InputManager;
import com.github.rmheuer.engine.core.input.keyboard.CharTypeEvent;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.keyboard.KeyPressEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyReleaseEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyRepeatEvent;
import com.github.rmheuer.engine.core.input.keyboard.Keyboard;
import com.github.rmheuer.engine.core.input.mouse.Mouse;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.input.mouse.MouseButtonPressEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseButtonReleaseEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseMoveEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseScrollEvent;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.render.Window;
import com.github.rmheuer.engine.render.WindowSettings;
import com.github.rmheuer.engine.render.event.WindowCloseEvent;
import com.github.rmheuer.engine.render.event.WindowFramebufferResizeEvent;
import com.github.rmheuer.engine.render.event.WindowResizeEvent;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class GLFWWindow implements Window {
    private final long window;

    private float cursorX;
    private float cursorY;

    public GLFWWindow(WindowSettings settings) {
        GLFWManager.require();

        glfwDefaultWindowHints();
        setContextWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, settings.isResizable() ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        window = glfwCreateWindow(
                settings.getWidth(),
                settings.getHeight(),
                settings.getTitle(),
                NULL,
                NULL
        );
        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        initContext();

        InputManager input = Game.get().getInputManager();
        input.registerSource(Mouse.class, new GLFWMouse(window));
        input.registerSource(Keyboard.class, new GLFWKeyboard(window));

        glfwSetWindowCloseCallback(window, this::windowCloseCallback);
        glfwSetWindowSizeCallback(window, this::windowSizeCallback);
        glfwSetFramebufferSizeCallback(window, this::framebufferSizeCallback);

        glfwSetCursorPosCallback(window, this::cursorPosCallback);
        glfwSetMouseButtonCallback(window, this::mouseButtonCallback);
        glfwSetScrollCallback(window, this::scrollCallback);

        glfwSetKeyCallback(window, this::keyCallback);
        glfwSetCharCallback(window, this::charCallback);

        // Send initial events
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(window, width, height);
            Game.get().postGlobalEvent(new WindowResizeEvent(this, width.get(0), height.get(0)));
            glfwGetFramebufferSize(window, width, height);
            Game.get().postGlobalEvent(new WindowFramebufferResizeEvent(this, width.get(0), height.get(0)));

            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(window, x, y);
            cursorX = (float) x.get(0);
            cursorY = (float) y.get(0);
            Game.get().postGlobalEvent(new MouseMoveEvent(cursorX, cursorY));
        }

        if (settings.isForceAspectRatio()) {
            glfwSetWindowAspectRatio(window, settings.getWidth(), settings.getHeight());
        }
        glfwShowWindow(window);
    }

    protected abstract void setContextWindowHints();

    protected abstract void initContext();

    private void windowCloseCallback(long window) {
        if (window != this.window)
            return;

        Game.get().postGlobalEvent(new WindowCloseEvent(this));
    }

    private void windowSizeCallback(long window, int width, int height) {
        if (window != this.window)
            return;

        Game.get().postGlobalEvent(new WindowResizeEvent(this, width, height));
    }

    private void framebufferSizeCallback(long window, int width, int height) {
        if (window != this.window)
            return;

        Game.get().postGlobalEvent(new WindowFramebufferResizeEvent(this, width, height));
    }

    private void cursorPosCallback(long window, double x, double y) {
        if (window != this.window)
            return;

        cursorX = (float) x;
        cursorY = (float) y;

        Game.get().postGlobalEvent(new MouseMoveEvent(cursorX, cursorY));
    }

    private void mouseButtonCallback(long window, int button, int action, int mods) {
        if (window != this.window)
            return;

        MouseButton mouseButton = GLFWMouse.getMouseButton(button);

        if (action == GLFW_PRESS) {
            Game.get().postGlobalEvent(new MouseButtonPressEvent(cursorX, cursorY, mouseButton));
        } else if (action == GLFW_RELEASE) {
            Game.get().postGlobalEvent(new MouseButtonReleaseEvent(cursorX, cursorY, mouseButton));
        }
    }

    private float convertScrollToPixels(double val) {
	return (float) (val * 120);
    }

    private void scrollCallback(long window, double x, double y) {
        if (window != this.window)
            return;

        Game.get().postGlobalEvent(new MouseScrollEvent(cursorX, cursorY, (float) x, (float) y, convertScrollToPixels(x), convertScrollToPixels(y)));
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        Key k = GLFWKeyboard.getKey(key);
        switch (action) {
            case GLFW_PRESS:
                Game.get().postGlobalEvent(new KeyPressEvent(k));
                break;
            case GLFW_REPEAT:
                Game.get().postGlobalEvent(new KeyRepeatEvent(k));
                break;
            case GLFW_RELEASE:
                Game.get().postGlobalEvent(new KeyReleaseEvent(k));
                break;
        }
    }

    private void charCallback(long window, int c) {
        Game.get().postGlobalEvent(new CharTypeEvent((char) c));
    }

    @Override
    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    @Override
    public int getWidth() {
        return getSize().x;
    }

    @Override
    public int getHeight() {
        return getSize().y;
    }

    @Override
    public Vector2i getSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(window, width, height);
            return new Vector2i(width.get(0), height.get(0));
        }
    }

    @Override
    public void close() {
        Callbacks.glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        GLFWManager.release();
    }
}
