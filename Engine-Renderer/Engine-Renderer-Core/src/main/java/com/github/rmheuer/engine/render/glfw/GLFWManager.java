package com.github.rmheuer.engine.render.glfw;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public final class GLFWManager {
    private static int refCount = 0;

    public static void require() {
	if (refCount == 0 && !glfwInit()) {
	    throw new RuntimeException("Failed to initialize GLFW");
	}

	refCount++;
    }

    public static void release() {
	refCount--;

	if (refCount == 0) {
	    glfwTerminate();
	}
    }

    private GLFWManager() {
	throw new AssertionError();
    }
}
