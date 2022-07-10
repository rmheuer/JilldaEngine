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

import com.github.rmheuer.engine.render.framebuffer.Framebuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

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
            Game.get().postEvent(new WindowResizeEvent(this, width.get(0), height.get(0)));
            glfwGetFramebufferSize(window, width, height);
            Game.get().postEvent(new WindowFramebufferResizeEvent(this, width.get(0), height.get(0)));

            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(window, x, y);
            cursorX = (float) x.get(0);
            cursorY = (float) y.get(0);
            Game.get().postEvent(new MouseMoveEvent(cursorX, cursorY));
        }

        glfwShowWindow(window);
    }

    protected abstract void setContextWindowHints();

    protected abstract void initContext();

    private void windowCloseCallback(long window) {
        if (window != this.window)
            return;

        Game.get().postEvent(new WindowCloseEvent(this));
    }

    private void windowSizeCallback(long window, int width, int height) {
        if (window != this.window)
            return;

        Game.get().postEvent(new WindowResizeEvent(this, width, height));
    }

    private void framebufferSizeCallback(long window, int width, int height) {
        if (window != this.window)
            return;

        Game.get().postEvent(new WindowFramebufferResizeEvent(this, width, height));
    }

    private void cursorPosCallback(long window, double x, double y) {
        if (window != this.window)
            return;

        cursorX = (float) x;
        cursorY = (float) y;

        Game.get().postEvent(new MouseMoveEvent(cursorX, cursorY));
    }

    private void mouseButtonCallback(long window, int button, int action, int mods) {
        if (window != this.window)
            return;

        MouseButton mouseButton = GLFWMouse.getMouseButton(button);

        if (action == GLFW_PRESS) {
            Game.get().postEvent(new MouseButtonPressEvent(cursorX, cursorY, mouseButton));
        } else if (action == GLFW_RELEASE) {
            Game.get().postEvent(new MouseButtonReleaseEvent(cursorX, cursorY, mouseButton));
        }
    }

    private float convertScrollToPixels(double val) {
        switch (Platform.get()) {
            case LINUX:
                return (float) val;
            case MACOSX:
                return (float) (val * 10);
            case WINDOWS:
                return (float) (val * 120);
        }
        return 0;
    }

    private void scrollCallback(long window, double x, double y) {
        if (window != this.window)
            return;

        Game.get().postEvent(new MouseScrollEvent(cursorX, cursorY, convertScrollToPixels(x), convertScrollToPixels(y)));
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        Key k = GLFWKeyboard.getKey(key);
        switch (action) {
            case GLFW_PRESS:
                Game.get().postEvent(new KeyPressEvent(k));
                break;
            case GLFW_REPEAT:
                Game.get().postEvent(new KeyRepeatEvent(k));
                break;
            case GLFW_RELEASE:
                Game.get().postEvent(new KeyReleaseEvent(k));
                break;
        }
    }

    private void charCallback(long window, int c) {
        Game.get().postEvent(new CharTypeEvent((char) c));
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
        glfwDestroyWindow(window);
        GLFWManager.release();
    }
}
