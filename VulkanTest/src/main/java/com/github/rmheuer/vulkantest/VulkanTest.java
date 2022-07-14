package com.github.rmheuer.vulkantest;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class VulkanTest {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private long window;

    private void initWindow() {
        glfwInit();

        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(WIDTH, HEIGHT, "Vulkan Test", NULL, NULL);
    }

    private void initVulkan() {

    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
        }
    }

    private void cleanUp() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void run() {
        initWindow();
        initVulkan();
        loop();
        cleanUp();
    }

    public static void main(String[] args) {
        VulkanTest app = new VulkanTest();
        app.run();
    }
}
