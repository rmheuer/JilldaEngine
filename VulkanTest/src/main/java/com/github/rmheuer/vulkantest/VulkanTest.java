package com.github.rmheuer.vulkantest;

import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkLayerProperties;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.EXTDebugUtils.VK_EXT_DEBUG_UTILS_EXTENSION_NAME;
import static org.lwjgl.vulkan.VK10.*;

public final class VulkanTest {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String TITLE = "Vulkan Test";

    private static final boolean ENABLE_VALIDATION = true;
    private static final String[] VALIDATION_NAMES = {
            "VK_LAYER_KHRONOS_validation"
    };

    private long window;
    private VkInstance instance;
    private VkPhysicalDevice physicalDevice;
    private VkDevice device;

    private void initWindow() {
        glfwInit();

        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
    }

    private void checkError(int err) {
        if (err != VK_SUCCESS) {
            throw new RuntimeException("Vulkan error: " + VulkanUtils.translateVulkanResult(err));
        }
    }

    private boolean checkValidationLayerSupport() {
        int[] layerCount = new int[1];
        vkEnumerateInstanceLayerProperties(layerCount, null);
        VkLayerProperties.Buffer availableLayers = VkLayerProperties.malloc(layerCount[0]);
        vkEnumerateInstanceLayerProperties(layerCount, availableLayers);

        List<String> layers = new ArrayList<>();
        System.out.println("Available Vulkan validation layers:");
        for (VkLayerProperties layer : availableLayers) {
            layers.add(layer.layerNameString());
            System.out.println("  - " + layer.layerNameString());
        }
        System.out.println();

        for (String layer : VALIDATION_NAMES) {
            if (!layers.contains(layer))
                return false;
        }
        return true;
    }

    private PointerBuffer getRequiredExtensions() {
        PointerBuffer glfwExtensions = glfwGetRequiredInstanceExtensions();
        int glfwExtensionCount = glfwExtensions.capacity();

        if (ENABLE_VALIDATION) {
            PointerBuffer result = memAllocPointer(glfwExtensionCount + 1);
            for (int i = 0; i < glfwExtensionCount; i++) {
                result.put(i, glfwExtensions.get(i));
            }
            result.put(glfwExtensionCount, stringToByteBuffer(VK_EXT_DEBUG_UTILS_EXTENSION_NAME));
            return result;
        } else {
            return glfwExtensions;
        }
    }

    private void createInstance() {
        if (ENABLE_VALIDATION && !checkValidationLayerSupport())
            throw new RuntimeException("Validation layers are not supported");

        VkApplicationInfo appInfo = VkApplicationInfo.calloc();
        ByteBuffer appName = stringToByteBuffer(TITLE);
        ByteBuffer engineName = stringToByteBuffer("No Engine");

        appInfo.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);
        appInfo.pApplicationName(appName);
        appInfo.applicationVersion(VK_MAKE_VERSION(1, 0, 0));
        appInfo.pEngineName(engineName);
        appInfo.engineVersion(VK_MAKE_VERSION(1, 0, 0));
        appInfo.apiVersion(VK_API_VERSION_1_0);

        VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.calloc();
        createInfo.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
        createInfo.pApplicationInfo(appInfo);
        if (ENABLE_VALIDATION) {
            // TODO: Do these need to be freed?
            PointerBuffer validationNames = memAllocPointer(VALIDATION_NAMES.length);
            for (int i = 0; i < VALIDATION_NAMES.length; i++) {
                ByteBuffer str = stringToByteBuffer(VALIDATION_NAMES[i]);
                validationNames.put(i, str);
            }
            createInfo.ppEnabledLayerNames(validationNames);
        }

        PointerBuffer requiredExtensions = getRequiredExtensions();
        createInfo.ppEnabledExtensionNames(requiredExtensions);

        PointerBuffer pInstance = memAllocPointer(1);
        int result = vkCreateInstance(createInfo, null, pInstance);
        checkError(result);
        long instancePtr = pInstance.get(0);
        instance = new VkInstance(instancePtr, createInfo);

        int[] extensionCount = new int[1];
        vkEnumerateInstanceExtensionProperties((ByteBuffer) null, extensionCount, null);
        VkExtensionProperties.Buffer extensions = VkExtensionProperties.malloc(extensionCount[0]);
        vkEnumerateInstanceExtensionProperties((ByteBuffer) null, extensionCount, extensions);

        System.out.println("Available Vulkan extensions:");
        for (VkExtensionProperties extension : extensions) {
            System.out.println("  - " + extension.extensionNameString());
        }
        System.out.println();

        extensions.free();
        memFree(pInstance);
        //requiredExtensions.free();
        memFree(engineName);
        memFree(appName);
        appInfo.free();
    }

    private Map<QueueFamily, Integer> findQueueFamilies(VkPhysicalDevice device) {
        Map<QueueFamily, Integer> indices = new HashMap<>();

        int[] queueFamilyCount = new int[1];
        vkGetPhysicalDeviceQueueFamilyProperties(device, queueFamilyCount, null);
        VkQueueFamilyProperties.Buffer queueFamilies = VkQueueFamilyProperties.malloc(queueFamilyCount[0]);
        vkGetPhysicalDeviceQueueFamilyProperties(device, queueFamilyCount, queueFamilies);

        int i = 0;
        for (VkQueueFamilyProperties queueFamily : queueFamilies) {
            QueueFamily family = null;

            int flags = queueFamily.queueFlags();
            if ((flags & VK_QUEUE_GRAPHICS_BIT) != 0)
                family = QueueFamily.GRAPHICS;

            if (family != null)
                indices.put(family, i);

            i++;
        }

        queueFamilies.free();
        return indices;
    }

    private boolean isDeviceSuitable(VkPhysicalDevice device) {
        Map<QueueFamily, Integer> indices = findQueueFamilies(device);
        return indices.containsKey(QueueFamily.GRAPHICS);
    }

    private void describeDevice(VkPhysicalDevice device, int index) {
        VkPhysicalDeviceProperties deviceProperties = VkPhysicalDeviceProperties.malloc();
        vkGetPhysicalDeviceProperties(device, deviceProperties);

        System.out.println(
                "  - [" + index + "] " +
                deviceProperties.deviceNameString() +
                " (API " + deviceProperties.apiVersion() + ")"
        );
    }

    private void pickPhysicalDevice() {
        int[] deviceCount = new int[1];
        vkEnumeratePhysicalDevices(instance, deviceCount, null);
        if (deviceCount[0] == 0) {
            throw new RuntimeException("Failed to find a GPU with Vulkan support");
        }
        System.out.println("Available physical devices: " + deviceCount[0]);

        PointerBuffer pDevices = memAllocPointer(deviceCount[0]);
        vkEnumeratePhysicalDevices(instance, deviceCount, pDevices);
        VkPhysicalDevice[] devices = new VkPhysicalDevice[deviceCount[0]];
        for (int i = 0; i < devices.length; i++) {
            long ptr = pDevices.get(i);
            devices[i] = new VkPhysicalDevice(ptr, instance);
        }
        pDevices.free();

        for (int i = 0; i < devices.length; i++) {
            describeDevice(devices[i], i);
        }

        int chosenIndex = -1;
        for (int i = 0; i < devices.length; i++) {
            if (isDeviceSuitable(devices[i])) {
                chosenIndex = i;
                physicalDevice = devices[i];
                break;
            }
        }

        System.out.println("Using device " + chosenIndex);
        System.out.println();
    }

    private void createLogicalDevice() {
        Map<QueueFamily, Integer> indices = findQueueFamilies(physicalDevice);

        VkDeviceQueueCreateInfo.Buffer pQueueCreateInfo = VkDeviceQueueCreateInfo.calloc(1);
        VkDeviceQueueCreateInfo queueCreateInfo = pQueueCreateInfo.get(0);
        queueCreateInfo.sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO);
        queueCreateInfo.queueFamilyIndex(indices.get(QueueFamily.GRAPHICS));
        FloatBuffer queuePriority = memAllocFloat(1);
        queuePriority.put(0, 1.0f);
        queueCreateInfo.pQueuePriorities(queuePriority);

        VkPhysicalDeviceFeatures deviceFeatures = VkPhysicalDeviceFeatures.calloc();

        VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.calloc();
        createInfo.sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO);
        createInfo.pQueueCreateInfos(pQueueCreateInfo);
        createInfo.pEnabledFeatures(deviceFeatures);
        if (ENABLE_VALIDATION) {
            PointerBuffer validationNames = memAllocPointer(VALIDATION_NAMES.length);
            for (int i = 0; i < VALIDATION_NAMES.length; i++) {
                ByteBuffer str = stringToByteBuffer(VALIDATION_NAMES[i]);
                validationNames.put(i, str);
            }
            createInfo.ppEnabledLayerNames(validationNames);
        }

        PointerBuffer pDevice = memAllocPointer(1);
        int result = vkCreateDevice(physicalDevice, createInfo, null, pDevice);
        checkError(result);
        device = new VkDevice(pDevice.get(0), physicalDevice, createInfo);

        memFree(pDevice);
        memFree(queuePriority);
    }

    private void initVulkan() {
        createInstance();
        pickPhysicalDevice();
        createLogicalDevice();
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
        }
    }

    private void cleanUp() {
        vkDestroyDevice(device, null);
        vkDestroyInstance(instance, null);

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void run() {
        initWindow();
        initVulkan();
        loop();
        cleanUp();
    }

    private ByteBuffer stringToByteBuffer(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.US_ASCII);
        ByteBuffer buf = memAlloc(bytes.length + 1);
        buf.put(bytes);
        buf.put((byte) 0); // Null terminator
        buf.flip();
        return buf;
    }

    public static void main(String[] args) {
        VulkanTest app = new VulkanTest();
        app.run();
    }
}
