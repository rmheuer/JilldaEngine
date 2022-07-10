package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.render.WindowSettings;
import com.github.rmheuer.engine.render.framebuffer.Framebuffer;
import com.github.rmheuer.engine.render.glfw.GLFWWindow;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

public final class OpenGLWindow extends GLFWWindow {
    private final OpenGLWindowFramebuffer framebuffer;

    public OpenGLWindow(WindowSettings settings) {
	super(settings);
	framebuffer = new OpenGLWindowFramebuffer(this);
    }

    @Override
    protected void setContextWindowHints() {
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
    }

    @Override
    protected void initContext() {
	GL.createCapabilities();
    }

    @Override
    public Framebuffer getDefaultFramebuffer() {
        return framebuffer;
    }
}
