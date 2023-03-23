package com.github.rmheuer.engine.render.vulkan;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.vulkan.EXTDebugUtils.*;
import static org.lwjgl.vulkan.VK10.*;

public final class VulkanContext {
    public static final String[] VALIDATION_LAYER_NAMES = {
            "VK_LAYER_KHRONOS_validation"
    };

    private final boolean enableValidationLayers;

    private VkInstance instance;
    private long debugMessenger;

    public VulkanContext(boolean enableValidationLayers) {
        this.enableValidationLayers = enableValidationLayers;

        createInstance();
        setUpDebugMessenger();
    }

    /**
     * Checks whether the validation layers are supported by the current
     * Vulkan context.
     *
     * @return whether all validation layers are supported
     */
    private boolean checkValidationLayerSupport() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Get the number of available layers
            IntBuffer pLayerCount = stack.mallocInt(1);
            vkEnumerateInstanceLayerProperties(pLayerCount, null);

            // Get the available layer properties
            VkLayerProperties.Buffer availableLayers = VkLayerProperties.malloc(pLayerCount.get(0), stack);
            vkEnumerateInstanceLayerProperties(pLayerCount, availableLayers);

            // Collect the names of all the available layers and print them out
            List<String> availLayers = new ArrayList<>();
            System.out.println("Available instance layers:");
            for (VkLayerProperties layer : availableLayers) {
                String name = layer.layerNameString();
                System.out.println("    " + name);
                availLayers.add(name);
            }

            // Check if all of the validation layers are present
            return availLayers.containsAll(Arrays.asList(VALIDATION_LAYER_NAMES));
        }
    }

    private PointerBuffer getRequiredExtensions(MemoryStack stack) {
        // Get the GLFW required extensions and store them in a list
        PointerBuffer glfwExtensions = glfwGetRequiredInstanceExtensions();
        List<String> extensionNames = new ArrayList<>();
        if (glfwExtensions != null) {
            for (int i = 0; i < glfwExtensions.capacity(); i++) {
                extensionNames.add(glfwExtensions.getStringASCII(i));
            }
        }

        // If we are enabling validation, we need the debug utils extension also
        if (enableValidationLayers) {
            extensionNames.add(VK_EXT_DEBUG_UTILS_EXTENSION_NAME);
        }

        // Store the extension names into a PointerBuffer
        PointerBuffer extensionsOut = stack.mallocPointer(extensionNames.size());
        for (String name : extensionNames) {
            extensionsOut.put(stack.ASCII(name));
        }
        extensionsOut.flip(); // Reset the position back to the start of the buffer

        return extensionsOut;
    }

    /**
     * Creates the Vulkan instance that we will use for rendering.
     */
    private void createInstance() {
        if (enableValidationLayers && !checkValidationLayerSupport())
            throw new RuntimeException("Validation layers enabled but not supported");

        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Define the information about this application for Vulkan
            VkApplicationInfo appInfo = VkApplicationInfo.calloc(stack);
            appInfo.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);

            appInfo.pApplicationName(stack.ASCII("Game"));
            appInfo.applicationVersion(VK_MAKE_VERSION(1, 0, 0));

            appInfo.pEngineName(stack.ASCII("JilldaEngine"));
            appInfo.engineVersion(VK_MAKE_VERSION(1, 0, 0));

            // We want to use the Vulkan 1.0 API
            appInfo.apiVersion(VK_API_VERSION_1_0);

            // Create a create info struct for the instance parameters
            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.calloc(stack);
            createInfo.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
            createInfo.pApplicationInfo(appInfo); // Pass the application info into the creation info struct

            // Enable the Vulkan extensions that GLFW requires to present a window
            createInfo.ppEnabledExtensionNames(getRequiredExtensions(stack));

            // If validation layers are enabled, we need to tell Vulkan that we want
            // to use them
            if (enableValidationLayers) {
                // Store all the validation layer names into a PointerBuffer
                PointerBuffer pLayerNames = stack.mallocPointer(VALIDATION_LAYER_NAMES.length);
                for (String name : VALIDATION_LAYER_NAMES) {
                    pLayerNames.put(stack.ASCII(name));
                }

                // Give the layer names to the create info struct
                createInfo.ppEnabledLayerNames(pLayerNames);
            }

            // Create the instance
            PointerBuffer pInstance = stack.mallocPointer(1);
            if (vkCreateInstance(createInfo, null, pInstance) != VK_SUCCESS)
                throw new RuntimeException("Failed to create instance");
            instance = new VkInstance(pInstance.get(0), createInfo);


            // Get the number of available instance extensions
            IntBuffer pExtensionCount = stack.mallocInt(1);
            vkEnumerateInstanceExtensionProperties((ByteBuffer) null, pExtensionCount, null);

            // Get all available instance extension properties
            VkExtensionProperties.Buffer extensions = VkExtensionProperties.malloc(pExtensionCount.get(0), stack);
            vkEnumerateInstanceExtensionProperties((ByteBuffer) null, pExtensionCount, extensions);

            // Print out the available instance extensions so we can see
            System.out.println("Available instance extensions:");
            for (VkExtensionProperties extension : extensions) {
                System.out.println("    " + extension.extensionNameString());
            }
        }
    }

    /**
     * Callback for debug messages from Vulkan and validation layers.
     *
     * @param severity the severity of the message
     * @param type the type of the message
     * @param pCallbackData pointer to the callback data struct
     * @param pUserData user data specified in create info struct
     * @return VK_FALSE, VK_TRUE is reserved for use by validation layers
     */
    private int debugCallback(int severity, int type, long pCallbackData, long pUserData) {
        VkDebugUtilsMessengerCallbackDataEXT callbackData = VkDebugUtilsMessengerCallbackDataEXT.create(pCallbackData);
        System.err.println("Vulkan: " + callbackData.pMessageString());

        return VK_FALSE;
    }

    /**
     * Sets up the debug messenger to receive debug messages from the
     * validation layers. This tells Vulkan to call the debugCallback()
     * method when there is a debug message.
     */
    private void setUpDebugMessenger() {
        // We don't need to set up the debug messenger if validation layers
        // are not enabled, since there will be no messages to show
        if (!enableValidationLayers)
            return;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Enable receiving messages of all severity levels
            int severity = VK_DEBUG_UTILS_MESSAGE_SEVERITY_VERBOSE_BIT_EXT
                    | VK_DEBUG_UTILS_MESSAGE_SEVERITY_INFO_BIT_EXT
                    | VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT
                    | VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT;

            // Enable receiving messages of all types
            int type = VK_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT
                    | VK_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT
                    | VK_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT;

            // Set up the creation parameters for the debug messenger
            VkDebugUtilsMessengerCreateInfoEXT createInfo = VkDebugUtilsMessengerCreateInfoEXT.calloc(stack);
            createInfo.sType(VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT);
            createInfo.messageSeverity(severity);
            createInfo.messageType(type);
            createInfo.pfnUserCallback(this::debugCallback); // Specify to call debugCallback()
            createInfo.pUserData(NULL); // We don't have any user data

            // Create the debug messenger
            LongBuffer pMessenger = stack.mallocLong(1);
            if (vkCreateDebugUtilsMessengerEXT(instance, createInfo, null, pMessenger) != VK_SUCCESS) {
                throw new RuntimeException("Failed to set up debug messenger");
            }
            debugMessenger = pMessenger.get(0);
        }
    }

    public void close() {
        if (enableValidationLayers)
            vkDestroyDebugUtilsMessengerEXT(instance, debugMessenger, null);

        vkDestroyInstance(instance, null);
    }

    public VkInstance getInstance() {
        return instance;
    }

    public boolean isValidationLayersEnabled() {
        return enableValidationLayers;
    }
}
