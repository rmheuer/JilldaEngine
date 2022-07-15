package com.github.rmheuer.vulkantest;

import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkAttachmentDescription;
import org.lwjgl.vulkan.VkAttachmentReference;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkClearValue;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkCommandBufferBeginInfo;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;
import org.lwjgl.vulkan.VkComponentMapping;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkFenceCreateInfo;
import org.lwjgl.vulkan.VkFramebufferCreateInfo;
import org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo;
import org.lwjgl.vulkan.VkImageSubresourceRange;
import org.lwjgl.vulkan.VkImageViewCreateInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkLayerProperties;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;
import org.lwjgl.vulkan.VkOffset2D;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState;
import org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineDynamicStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;
import org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineViewportStateCreateInfo;
import org.lwjgl.vulkan.VkPresentInfoKHR;
import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkQueueFamilyProperties;
import org.lwjgl.vulkan.VkRect2D;
import org.lwjgl.vulkan.VkRenderPassBeginInfo;
import org.lwjgl.vulkan.VkRenderPassCreateInfo;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;
import org.lwjgl.vulkan.VkShaderModuleCreateInfo;
import org.lwjgl.vulkan.VkSubmitInfo;
import org.lwjgl.vulkan.VkSubpassDependency;
import org.lwjgl.vulkan.VkSubpassDescription;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;
import org.lwjgl.vulkan.VkVertexInputAttributeDescription;
import org.lwjgl.vulkan.VkVertexInputBindingDescription;
import org.lwjgl.vulkan.VkViewport;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.EXTDebugUtils.VK_EXT_DEBUG_UTILS_EXTENSION_NAME;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.VK10.*;

public final class VulkanTest {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String TITLE = "Vulkan Test";

    private static final boolean ENABLE_VALIDATION = true;
    private static final String[] VALIDATION_NAMES = {
            "VK_LAYER_KHRONOS_validation"
    };

    private static final String[] EXTENSION_NAMES = {
            VK_KHR_SWAPCHAIN_EXTENSION_NAME
    };

    private static final Vertex[] VERTICES = {
            new Vertex(new Vector2f( 0.0f, -0.5f), new Vector3f(1.0f, 1.0f, 1.0f)),
            new Vertex(new Vector2f( 0.5f,  0.5f), new Vector3f(0.0f, 1.0f, 0.0f)),
            new Vertex(new Vector2f(-0.5f,  0.5f), new Vector3f(0.0f, 0.0f, 1.0f))
    };

    private long window;
    private VkInstance instance;
    private long surface;
    private VkPhysicalDevice physicalDevice;
    private VkDevice device;
    private VkQueue graphicsQueue;
    private VkQueue presentQueue;
    private long swapChain;
    private long[] swapChainImages;
    private int swapChainImageFormat;
    private VkExtent2D swapChainExtent;
    private long[] swapChainImageViews;
    private long renderPass;
    private long pipelineLayout;
    private long graphicsPipeline;
    private long[] swapChainFramebuffers;
    private long commandPool;
    private VkCommandBuffer commandBuffer;
    private long imageAvailableSemaphore;
    private long renderFinishedSemaphore;
    private long inFlightFence;
    private boolean framebufferResized = false;
    private long vertexBuffer;
    private long vertexBufferMemory;

    private void framebufferResizeCallback(long window, int width, int height) {
        framebufferResized = true;
    }

    private void initWindow() {
        glfwInit();

        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
        glfwSetFramebufferSizeCallback(window, this::framebufferResizeCallback);
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

    private void createSurface() {
        long[] pSurface = new long[1];
        int result = glfwCreateWindowSurface(instance, window, null, pSurface);
        checkError(result);
        surface = pSurface[0];
    }

    private Map<QueueFamily, Integer> findQueueFamilies(VkPhysicalDevice device) {
        Map<QueueFamily, Integer> indices = new HashMap<>();

        int[] queueFamilyCount = new int[1];
        vkGetPhysicalDeviceQueueFamilyProperties(device, queueFamilyCount, null);
        VkQueueFamilyProperties.Buffer queueFamilies = VkQueueFamilyProperties.malloc(queueFamilyCount[0]);
        vkGetPhysicalDeviceQueueFamilyProperties(device, queueFamilyCount, queueFamilies);

        int i = 0;
        for (VkQueueFamilyProperties queueFamily : queueFamilies) {
            int flags = queueFamily.queueFlags();
            if ((flags & VK_QUEUE_GRAPHICS_BIT) != 0)
                indices.put(QueueFamily.GRAPHICS, i);

            int[] pSupported = new int[1];
            vkGetPhysicalDeviceSurfaceSupportKHR(device, i, surface, pSupported);
            if (pSupported[0] != 0) {
                indices.put(QueueFamily.PRESENT, i);
            }

            i++;
        }

        queueFamilies.free();
        return indices;
    }

    private boolean checkDeviceExtensionSupport(VkPhysicalDevice device) {
        int[] extensionCount = new int[1];
        vkEnumerateDeviceExtensionProperties(device, (ByteBuffer) null, extensionCount, null);
        VkExtensionProperties.Buffer availableExtensions = VkExtensionProperties.malloc(extensionCount[0]);
        vkEnumerateDeviceExtensionProperties(device, (ByteBuffer) null, extensionCount, availableExtensions);

        List<String> extensions = new ArrayList<>();
        System.out.println("Device extensions:");
        for (VkExtensionProperties extension : availableExtensions) {
            extensions.add(extension.extensionNameString());
            System.out.println("  - " + extension.extensionNameString());
        }

        for (String extension : EXTENSION_NAMES) {
            if (!extensions.contains(extension)) {
                return false;
            }
        }

        return true;
    }

    private SwapChainSupportInfo querySwapChainSupport(VkPhysicalDevice device) {
        SwapChainSupportInfo info = new SwapChainSupportInfo();

        info.capabilities = VkSurfaceCapabilitiesKHR.malloc();
        vkGetPhysicalDeviceSurfaceCapabilitiesKHR(device, surface, info.capabilities);

        int[] formatCount = new int[1];
        vkGetPhysicalDeviceSurfaceFormatsKHR(device, surface, formatCount, null);
        if (formatCount[0] != 0) {
            info.formats = VkSurfaceFormatKHR.malloc(formatCount[0]);
            vkGetPhysicalDeviceSurfaceFormatsKHR(device, surface, formatCount, info.formats);
        }

        int[] presentModeCount = new int[1];
        vkGetPhysicalDeviceSurfacePresentModesKHR(device, surface, presentModeCount, null);
        if (presentModeCount[0] != 0) {
            info.presentModes = new int[presentModeCount[0]];
            vkGetPhysicalDeviceSurfacePresentModesKHR(device, surface, presentModeCount, info.presentModes);
        }

        return info;
    }

    private boolean isDeviceSuitable(VkPhysicalDevice device) {
        Map<QueueFamily, Integer> indices = findQueueFamilies(device);

        boolean extensionsSupported = checkDeviceExtensionSupport(device);

        boolean swapChainAdequate = false;
        if (extensionsSupported) {
            SwapChainSupportInfo info = querySwapChainSupport(device);
            swapChainAdequate = info.formats.capacity() != 0 && info.presentModes.length != 0;
            info.free();
        }

        return indices.containsKey(QueueFamily.GRAPHICS) && extensionsSupported && swapChainAdequate;
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

    private VkQueue getQueue(int idx, PointerBuffer p) {
        vkGetDeviceQueue(device, idx, 0, p);
        return new VkQueue(p.get(0), device);
    }

    private void createLogicalDevice() {
        Map<QueueFamily, Integer> indices = findQueueFamilies(physicalDevice);

        Set<Integer> uniqueQueueFamilies = new HashSet<>(indices.values());

        FloatBuffer queuePriority = memAllocFloat(1);
        queuePriority.put(0, 1.0f);

        VkDeviceQueueCreateInfo.Buffer pQueueCreateInfo = VkDeviceQueueCreateInfo.calloc(uniqueQueueFamilies.size());
        int queueIdx = 0;
        for (int family : uniqueQueueFamilies) {
            VkDeviceQueueCreateInfo queueCreateInfo = pQueueCreateInfo.get(queueIdx);
            queueCreateInfo.sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO);
            queueCreateInfo.queueFamilyIndex(family);
            queueCreateInfo.pQueuePriorities(queuePriority);

            queueIdx++;
        }

        VkPhysicalDeviceFeatures deviceFeatures = VkPhysicalDeviceFeatures.calloc();

        VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.calloc();
        createInfo.sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO);
        createInfo.pQueueCreateInfos(pQueueCreateInfo);
        createInfo.pEnabledFeatures(deviceFeatures);
        PointerBuffer extensionNames = memAllocPointer(EXTENSION_NAMES.length);
        for (int i = 0; i < EXTENSION_NAMES.length; i++) {
            ByteBuffer str = stringToByteBuffer(EXTENSION_NAMES[i]);
            extensionNames.put(i, str);
        }
        createInfo.ppEnabledExtensionNames(extensionNames);
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

        PointerBuffer pQueue = memAllocPointer(1);
        graphicsQueue = getQueue(indices.get(QueueFamily.GRAPHICS), pQueue);
        presentQueue = getQueue(indices.get(QueueFamily.PRESENT), pQueue);

        memFree(pQueue);
        memFree(pDevice);
        memFree(queuePriority);
    }

    private VkSurfaceFormatKHR chooseSwapSurfaceFormat(VkSurfaceFormatKHR.Buffer formats) {
        for (VkSurfaceFormatKHR format : formats) {
            if (format.format() == VK_FORMAT_R8G8B8A8_SRGB && format.colorSpace() == VK_COLOR_SPACE_SRGB_NONLINEAR_KHR) {
                return format;
            }
        }

        // Fallback to first available format
        return formats.get(0);
    }

    private int chooseSwapPresentMode(int[] modes) {
        for (int mode : modes) {
            if (mode == VK_PRESENT_MODE_MAILBOX_KHR) {
                return mode;
            }
        }

        // Fallback to FIFO, as it is always supported
        return VK_PRESENT_MODE_FIFO_KHR;
    }

    private VkExtent2D chooseSwapExtent(VkSurfaceCapabilitiesKHR capabilities) {
        if (capabilities.currentExtent().width() != 0xFFFFFFFF) {
            return capabilities.currentExtent();
        } else {
            int[] pWidth = new int[1];
            int[] pHeight = new int[1];
            glfwGetFramebufferSize(window, pWidth, pHeight);

            VkExtent2D min = capabilities.minImageExtent();
            VkExtent2D max = capabilities.maxImageExtent();
            int width = MathUtils.clamp(pWidth[0], min.width(), max.width());
            int height = MathUtils.clamp(pHeight[0], min.height(), max.height());

            VkExtent2D extent = VkExtent2D.create();
            extent.width(width);
            extent.height(height);

            return extent;
        }
    }

    private void createSwapChain() {
        SwapChainSupportInfo info = querySwapChainSupport(physicalDevice);

        VkSurfaceFormatKHR surfaceFormat = chooseSwapSurfaceFormat(info.formats);
        int presentMode = chooseSwapPresentMode(info.presentModes);
        VkExtent2D extent = chooseSwapExtent(info.capabilities);

        int imageCount = info.capabilities.minImageCount() + 1;
        int maxImageCount = info.capabilities.maxImageCount();
        if (maxImageCount > 0 && imageCount > maxImageCount)
            imageCount = maxImageCount;

        VkSwapchainCreateInfoKHR createInfo = VkSwapchainCreateInfoKHR.calloc();
        createInfo.sType(VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR);
        createInfo.surface(surface);
        createInfo.minImageCount(imageCount);
        createInfo.imageFormat(surfaceFormat.format());
        createInfo.imageColorSpace(surfaceFormat.colorSpace());
        createInfo.imageExtent(extent);
        createInfo.imageArrayLayers(1);
        createInfo.imageUsage(VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT);

        Map<QueueFamily, Integer> indices = findQueueFamilies(physicalDevice);
        int graphics = indices.get(QueueFamily.GRAPHICS);
        int present = indices.get(QueueFamily.PRESENT);
        IntBuffer queueFamilyIndices = memAllocInt(2);
        queueFamilyIndices.put(0, graphics);
        queueFamilyIndices.put(1, present);
        if (graphics != present) {
            createInfo.imageSharingMode(VK_SHARING_MODE_CONCURRENT);
            createInfo.queueFamilyIndexCount(2);
            createInfo.pQueueFamilyIndices(queueFamilyIndices);
        } else {
            createInfo.imageSharingMode(VK_SHARING_MODE_EXCLUSIVE);
        }
        createInfo.preTransform(info.capabilities.currentTransform());
        createInfo.compositeAlpha(VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR);
        createInfo.presentMode(presentMode);
        createInfo.clipped(true);
        createInfo.oldSwapchain(NULL);

        long[] pSwapChain = new long[1];
        int result = vkCreateSwapchainKHR(device, createInfo, null, pSwapChain);
        checkError(result);
        swapChain = pSwapChain[0];

        int[] pImageCount = new int[1];
        vkGetSwapchainImagesKHR(device, swapChain, pImageCount, null);
        swapChainImages = new long[pImageCount[0]];
        vkGetSwapchainImagesKHR(device, swapChain, pImageCount, swapChainImages);

        swapChainImageFormat = surfaceFormat.format();
        swapChainExtent = extent;
    }

    private void createImageViews() {
        swapChainImageViews = new long[swapChainImages.length];
        for (int i = 0; i < swapChainImages.length; i++) {
            VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.calloc();
            createInfo.sType(VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO);
            createInfo.image(swapChainImages[i]);
            createInfo.viewType(VK_IMAGE_VIEW_TYPE_2D);
            createInfo.format(swapChainImageFormat);
            VkComponentMapping components = createInfo.components();
            components.r(VK_COMPONENT_SWIZZLE_IDENTITY);
            components.g(VK_COMPONENT_SWIZZLE_IDENTITY);
            components.b(VK_COMPONENT_SWIZZLE_IDENTITY);
            components.a(VK_COMPONENT_SWIZZLE_IDENTITY);
            VkImageSubresourceRange subresourceRange = createInfo.subresourceRange();
            subresourceRange.aspectMask(VK_IMAGE_ASPECT_COLOR_BIT);
            subresourceRange.baseMipLevel(0);
            subresourceRange.levelCount(1);
            subresourceRange.baseArrayLayer(0);
            subresourceRange.layerCount(1);

            long[] pView = new long[1];
            int result = vkCreateImageView(device, createInfo, null, pView);
            checkError(result);
            swapChainImageViews[i] = pView[0];
        }
    }

    private long createShaderModule(ByteBuffer code) {
        VkShaderModuleCreateInfo createInfo = VkShaderModuleCreateInfo.calloc();
        createInfo.sType(VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO);
        createInfo.pCode(code);

        long[] pShaderModule = new long[1];
        int result = vkCreateShaderModule(device, createInfo, null, pShaderModule);
        checkError(result);

        return pShaderModule[0];
    }

    private void createRenderPass() {
        VkAttachmentDescription.Buffer pColorAttachment = VkAttachmentDescription.calloc(1);
        VkAttachmentDescription colorAttachment = pColorAttachment.get(0);
        colorAttachment.format(swapChainImageFormat);
        colorAttachment.samples(VK_SAMPLE_COUNT_1_BIT);
        colorAttachment.loadOp(VK_ATTACHMENT_LOAD_OP_CLEAR);
        colorAttachment.storeOp(VK_ATTACHMENT_STORE_OP_STORE);
        colorAttachment.stencilLoadOp(VK_ATTACHMENT_LOAD_OP_DONT_CARE);
        colorAttachment.stencilStoreOp(VK_ATTACHMENT_STORE_OP_DONT_CARE);
        colorAttachment.initialLayout(VK_IMAGE_LAYOUT_UNDEFINED);
        colorAttachment.finalLayout(VK_IMAGE_LAYOUT_PRESENT_SRC_KHR);

        VkAttachmentReference.Buffer pColorAttachmentRef = VkAttachmentReference.calloc(1);
        VkAttachmentReference colorAttachmentRef = pColorAttachmentRef.get(0);
        colorAttachmentRef.attachment(0);
        colorAttachmentRef.layout(VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL);

        VkSubpassDescription.Buffer pSubpass = VkSubpassDescription.calloc(1);
        VkSubpassDescription subpass = pSubpass.get(0);
        subpass.pipelineBindPoint(VK_PIPELINE_BIND_POINT_GRAPHICS);
        subpass.colorAttachmentCount(1);
        subpass.pColorAttachments(pColorAttachmentRef);

        VkRenderPassCreateInfo renderPassInfo = VkRenderPassCreateInfo.calloc();
        renderPassInfo.sType(VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO);
        renderPassInfo.pAttachments(pColorAttachment);
        renderPassInfo.pSubpasses(pSubpass);

        VkSubpassDependency.Buffer pDependency = VkSubpassDependency.calloc(1);
        VkSubpassDependency dependency = pDependency.get(0);
        dependency.srcSubpass(VK_SUBPASS_EXTERNAL);
        dependency.dstSubpass(0);
        dependency.srcStageMask(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
        dependency.srcAccessMask(0);
        dependency.dstStageMask(VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);
        dependency.dstAccessMask(VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT);

        renderPassInfo.pDependencies(pDependency);

        long[] pRenderPass = new long[1];
        int result = vkCreateRenderPass(device, renderPassInfo, null, pRenderPass);
        checkError(result);
        renderPass = pRenderPass[0];
    }

    private void createGraphicsPipeline() {
        ByteBuffer vertShaderCode = ShaderCompiler.compile(new JarResourceFile("vertex.glsl"), ShaderCompiler.Type.Vertex);
        ByteBuffer fragShaderCode = ShaderCompiler.compile(new JarResourceFile("fragment.glsl"), ShaderCompiler.Type.Fragment);

        long vertShaderModule = createShaderModule(vertShaderCode);
        long fragShaderModule = createShaderModule(fragShaderCode);

        VkPipelineShaderStageCreateInfo.Buffer shaderStages = VkPipelineShaderStageCreateInfo.calloc(2);
        VkPipelineShaderStageCreateInfo vertShaderStageInfo = shaderStages.get(0);
        vertShaderStageInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO);
        vertShaderStageInfo.stage(VK_SHADER_STAGE_VERTEX_BIT);
        vertShaderStageInfo.module(vertShaderModule);
        ByteBuffer pName = stringToByteBuffer("main");
        vertShaderStageInfo.pName(pName);

        VkPipelineShaderStageCreateInfo fragShaderStageInfo = shaderStages.get(1);
        fragShaderStageInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO);
        fragShaderStageInfo.stage(VK_SHADER_STAGE_FRAGMENT_BIT);
        fragShaderStageInfo.module(fragShaderModule);
        fragShaderStageInfo.pName(pName);

        VkPipelineVertexInputStateCreateInfo vertexInputInfo = VkPipelineVertexInputStateCreateInfo.calloc();
        vertexInputInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO);

        VkVertexInputBindingDescription.Buffer pBindingDesc = VkVertexInputBindingDescription.calloc(1);
        Vertex.getBindingDescription(pBindingDesc.get(0));
        VkVertexInputAttributeDescription.Buffer attributeDesc = Vertex.getAttributeDescriptions();

        vertexInputInfo.pVertexBindingDescriptions(pBindingDesc);
        vertexInputInfo.pVertexAttributeDescriptions(attributeDesc);

        VkPipelineInputAssemblyStateCreateInfo inputAssembly = VkPipelineInputAssemblyStateCreateInfo.calloc();
        inputAssembly.sType(VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO);
        inputAssembly.topology(VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST);
        inputAssembly.primitiveRestartEnable(false);

        VkViewport.Buffer pViewport = VkViewport.calloc(1);
        VkViewport viewport = pViewport.get(0);
        viewport.x(0);
        viewport.y(0);
        viewport.width(swapChainExtent.width());
        viewport.height(swapChainExtent.height());
        viewport.minDepth(0.0f);
        viewport.maxDepth(1.0f);

        VkRect2D.Buffer pScissor = VkRect2D.calloc(1);
        VkRect2D scissor = pScissor.get(0);
        VkOffset2D scissorOffset = VkOffset2D.calloc();
        scissorOffset.set(0, 0);
        scissor.offset(scissorOffset);
        scissor.extent(swapChainExtent);

        VkPipelineViewportStateCreateInfo viewportState = VkPipelineViewportStateCreateInfo.calloc();
        viewportState.sType(VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO);
        viewportState.pViewports(pViewport);
        viewportState.pScissors(pScissor);

        VkPipelineRasterizationStateCreateInfo rasterizer = VkPipelineRasterizationStateCreateInfo.calloc();
        rasterizer.sType(VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO);
        rasterizer.depthClampEnable(false);
        rasterizer.rasterizerDiscardEnable(false);
        rasterizer.polygonMode(VK_POLYGON_MODE_FILL);
        rasterizer.lineWidth(1);
        rasterizer.cullMode(VK_CULL_MODE_BACK_BIT);
        rasterizer.frontFace(VK_FRONT_FACE_CLOCKWISE);
        rasterizer.depthBiasEnable(false);

        VkPipelineMultisampleStateCreateInfo multisampling = VkPipelineMultisampleStateCreateInfo.calloc();
        multisampling.sType(VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO);
        multisampling.sampleShadingEnable(false);
        multisampling.rasterizationSamples(VK_SAMPLE_COUNT_1_BIT);

        VkPipelineColorBlendAttachmentState.Buffer pColorBlendAttachment = VkPipelineColorBlendAttachmentState.calloc(1);
        VkPipelineColorBlendAttachmentState colorBlendAttachment = pColorBlendAttachment.get(0);
        colorBlendAttachment.colorWriteMask(VK_COLOR_COMPONENT_R_BIT | VK_COLOR_COMPONENT_G_BIT | VK_COLOR_COMPONENT_B_BIT | VK_COLOR_COMPONENT_A_BIT);
        colorBlendAttachment.blendEnable(false);

        VkPipelineColorBlendStateCreateInfo colorBlending = VkPipelineColorBlendStateCreateInfo.calloc();
        colorBlending.sType(VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO);
        colorBlending.logicOpEnable(false);
        colorBlending.pAttachments(pColorBlendAttachment);

        VkPipelineLayoutCreateInfo pipelineLayoutInfo = VkPipelineLayoutCreateInfo.calloc();
        pipelineLayoutInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO);

        long[] pPipelineLayout = new long[1];
        int result = vkCreatePipelineLayout(device, pipelineLayoutInfo, null, pPipelineLayout);
        checkError(result);
        pipelineLayout = pPipelineLayout[0];

        IntBuffer dynamicStates = memAllocInt(2);
        dynamicStates.put(0, VK_DYNAMIC_STATE_VIEWPORT);
        dynamicStates.put(1, VK_DYNAMIC_STATE_SCISSOR);
        VkPipelineDynamicStateCreateInfo dynamicState = VkPipelineDynamicStateCreateInfo.calloc();
        dynamicState.sType(VK_STRUCTURE_TYPE_PIPELINE_DYNAMIC_STATE_CREATE_INFO);
        dynamicState.pDynamicStates(dynamicStates);

        VkGraphicsPipelineCreateInfo.Buffer pPipelineInfo = VkGraphicsPipelineCreateInfo.calloc(1);
        VkGraphicsPipelineCreateInfo pipelineInfo = pPipelineInfo.get(0);
        pipelineInfo.sType(VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO);
        pipelineInfo.pStages(shaderStages);
        pipelineInfo.pVertexInputState(vertexInputInfo);
        pipelineInfo.pInputAssemblyState(inputAssembly);
        pipelineInfo.pViewportState(viewportState);
        pipelineInfo.pRasterizationState(rasterizer);
        pipelineInfo.pMultisampleState(multisampling);
        pipelineInfo.pDepthStencilState(null);
        pipelineInfo.pColorBlendState(colorBlending);
        pipelineInfo.pDynamicState(dynamicState);
        pipelineInfo.layout(pipelineLayout);
        pipelineInfo.renderPass(renderPass);
        pipelineInfo.subpass(0);

        long[] pGraphicsPipeline = new long[1];
        result = vkCreateGraphicsPipelines(device, VK_NULL_HANDLE, pPipelineInfo, null, pGraphicsPipeline);
        checkError(result);
        graphicsPipeline = pGraphicsPipeline[0];

        vkDestroyShaderModule(device, vertShaderModule, null);
        vkDestroyShaderModule(device, fragShaderModule, null);
    }

    private void createFramebuffers() {
        swapChainFramebuffers = new long[swapChainImageViews.length];

        for (int i = 0; i < swapChainImageViews.length; i++) {
            LongBuffer attachments = memAllocLong(1);
            attachments.put(0, swapChainImageViews[i]);

            VkFramebufferCreateInfo framebufferInfo = VkFramebufferCreateInfo.calloc();
            framebufferInfo.sType(VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO);
            framebufferInfo.renderPass(renderPass);
            framebufferInfo.attachmentCount(1);
            framebufferInfo.pAttachments(attachments);
            framebufferInfo.width(swapChainExtent.width());
            framebufferInfo.height(swapChainExtent.height());
            framebufferInfo.layers(1);

            long[] pFramebuffer = new long[1];
            int result = vkCreateFramebuffer(device, framebufferInfo, null, pFramebuffer);
            checkError(result);
            swapChainFramebuffers[i] = pFramebuffer[0];
        }
    }

    private void createCommandPool() {
        Map<QueueFamily, Integer> queueFamilyIndices = findQueueFamilies(physicalDevice);

        VkCommandPoolCreateInfo poolInfo = VkCommandPoolCreateInfo.calloc();
        poolInfo.sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO);
        poolInfo.flags(VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT);
        poolInfo.queueFamilyIndex(queueFamilyIndices.get(QueueFamily.GRAPHICS));

        long[] pCommandPool = new long[1];
        int result = vkCreateCommandPool(device, poolInfo, null, pCommandPool);
        checkError(result);
        commandPool = pCommandPool[0];
    }

    private int findMemoryType(int typeFilter, int properties) {
        VkPhysicalDeviceMemoryProperties memProperties = VkPhysicalDeviceMemoryProperties.malloc();
        vkGetPhysicalDeviceMemoryProperties(physicalDevice, memProperties);

        int size = memProperties.memoryTypeCount();

        for (int i = 0; i < size; i++) {
            if ((typeFilter & (1 << i)) != 0 && (memProperties.memoryTypes(i).propertyFlags() & properties) == properties) {
                memProperties.free();
                return i;
            }
        }

        throw new RuntimeException("Failed to find suitable memory type");
    }

    private void createVertexBuffer() {
        int size = VERTICES.length * Vertex.SIZEOF;

        VkBufferCreateInfo bufferInfo = VkBufferCreateInfo.calloc();
        bufferInfo.sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO);
        bufferInfo.size(size);
        bufferInfo.usage(VK_BUFFER_USAGE_VERTEX_BUFFER_BIT);
        bufferInfo.sharingMode(VK_SHARING_MODE_EXCLUSIVE);

        long[] pVertexBuffer = new long[1];
        int result = vkCreateBuffer(device, bufferInfo, null, pVertexBuffer);
        checkError(result);
        vertexBuffer = pVertexBuffer[0];

        bufferInfo.free();

        VkMemoryRequirements memRequirements = VkMemoryRequirements.malloc();
        vkGetBufferMemoryRequirements(device, vertexBuffer, memRequirements);

        VkMemoryAllocateInfo allocInfo = VkMemoryAllocateInfo.calloc();
        allocInfo.sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO);
        allocInfo.allocationSize(memRequirements.size());
        allocInfo.memoryTypeIndex(findMemoryType(memRequirements.memoryTypeBits(), VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT));

        long[] pMemory = new long[1];
        result = vkAllocateMemory(device, allocInfo, null, pMemory);
        checkError(result);
        vertexBufferMemory = pMemory[0];

        vkBindBufferMemory(device, vertexBuffer, vertexBufferMemory, 0);

        PointerBuffer pData = memAllocPointer(1);
        vkMapMemory(device, vertexBufferMemory, 0, size, 0, pData);
        ByteBuffer data = pData.getByteBuffer(0, size);
        data.position(0);
        data.limit(size);
        for (Vertex v : VERTICES) {
            v.addToBuffer(data);
        }
        vkUnmapMemory(device, vertexBufferMemory);

        pData.free();
        allocInfo.free();
        memRequirements.free();
    }

    private void createCommandBuffer() {
        VkCommandBufferAllocateInfo allocInfo = VkCommandBufferAllocateInfo.calloc();
        allocInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO);
        allocInfo.commandPool(commandPool);
        allocInfo.level(VK_COMMAND_BUFFER_LEVEL_PRIMARY);
        allocInfo.commandBufferCount(1);

        PointerBuffer pCommandBuffer = memAllocPointer(1);
        int result = vkAllocateCommandBuffers(device, allocInfo, pCommandBuffer);
        checkError(result);

        commandBuffer = new VkCommandBuffer(pCommandBuffer.get(0), device);
    }

    private void createSyncObjects() {
        VkSemaphoreCreateInfo semaphoreInfo = VkSemaphoreCreateInfo.calloc();
        semaphoreInfo.sType(VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO);

        VkFenceCreateInfo fenceInfo = VkFenceCreateInfo.calloc();
        fenceInfo.sType(VK_STRUCTURE_TYPE_FENCE_CREATE_INFO);
        fenceInfo.flags(VK_FENCE_CREATE_SIGNALED_BIT);

        long[] p = new long[1];

        int result = vkCreateSemaphore(device, semaphoreInfo, null, p);
        checkError(result);
        imageAvailableSemaphore = p[0];

        result = vkCreateSemaphore(device, semaphoreInfo, null, p);
        checkError(result);
        renderFinishedSemaphore = p[0];

        result = vkCreateFence(device, fenceInfo, null, p);
        checkError(result);
        inFlightFence = p[0];
    }

    private void initVulkan() {
        createInstance();
        createSurface();
        pickPhysicalDevice();
        createLogicalDevice();
        createSwapChain();
        createImageViews();
        createRenderPass();
        createGraphicsPipeline();
        createFramebuffers();
        createCommandPool();
        createVertexBuffer();
        createCommandBuffer();
        createSyncObjects();
    }

    private void recordCommandBuffer(VkCommandBuffer commandBuffer, int imageIndex) {
        VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo.calloc();
        beginInfo.sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO);
        beginInfo.flags(0);
        beginInfo.pInheritanceInfo(null);
        int result = vkBeginCommandBuffer(commandBuffer, beginInfo);
        checkError(result);
        beginInfo.free();

        VkRenderPassBeginInfo renderPassInfo = VkRenderPassBeginInfo.calloc();
        renderPassInfo.sType(VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO);
        renderPassInfo.renderPass(renderPass);
        renderPassInfo.framebuffer(swapChainFramebuffers[imageIndex]);
        renderPassInfo.renderArea().offset().set(0, 0);
        renderPassInfo.renderArea().extent(swapChainExtent);

        VkClearValue.Buffer pClearColor = VkClearValue.calloc(1);
        VkClearValue clearColor = pClearColor.get(0);
        FloatBuffer colorData = memAllocFloat(4);
        colorData.put(0, 0.1f);
        colorData.put(1, 0.2f);
        colorData.put(2, 0.3f);
        colorData.put(3, 1.0f);
        clearColor.color().float32(colorData);
        renderPassInfo.clearValueCount(1);
        renderPassInfo.pClearValues(pClearColor);

        vkCmdBeginRenderPass(commandBuffer, renderPassInfo, VK_SUBPASS_CONTENTS_INLINE);

        memFree(colorData);
        pClearColor.free();
        renderPassInfo.free();

        vkCmdBindPipeline(commandBuffer, VK_PIPELINE_BIND_POINT_GRAPHICS, graphicsPipeline);

        VkViewport.Buffer pViewport = VkViewport.calloc(1);
        VkViewport viewport = pViewport.get(0);
        viewport.x(0);
        viewport.y(0);
        viewport.width(swapChainExtent.width());
        viewport.height(swapChainExtent.height());
        viewport.minDepth(0.0f);
        viewport.maxDepth(1.0f);
        vkCmdSetViewport(commandBuffer, 0, pViewport);

        VkRect2D.Buffer pScissor = VkRect2D.calloc(1);
        VkRect2D scissor = pScissor.get(0);
        scissor.offset().set(0, 0);
        scissor.extent(swapChainExtent);
        vkCmdSetScissor(commandBuffer, 0, pScissor);

        long[] vertexBuffers = {vertexBuffer};
        long[] offsets = {0};
        vkCmdBindVertexBuffers(commandBuffer, 0, vertexBuffers, offsets);

        // DRAW IT!!!!!
        vkCmdDraw(commandBuffer, VERTICES.length, 1, 0, 0);

        vkCmdEndRenderPass(commandBuffer);
        result = vkEndCommandBuffer(commandBuffer);
        checkError(result);
    }

    private void drawFrame() {
        long[] pFence = {inFlightFence};
        vkWaitForFences(device, pFence, true, Long.MAX_VALUE);

        int[] pImageIndex = new int[1];
        int result = vkAcquireNextImageKHR(device, swapChain, Long.MAX_VALUE, imageAvailableSemaphore, VK_NULL_HANDLE, pImageIndex);
        if (result == VK_ERROR_OUT_OF_DATE_KHR || result == VK_SUBOPTIMAL_KHR || framebufferResized) {
            framebufferResized = false;
            recreateSwapChain();
            return;
        } else {
            checkError(result);
        }

        vkResetFences(device, pFence);
        vkResetCommandBuffer(commandBuffer, 0);
        recordCommandBuffer(commandBuffer, pImageIndex[0]);

        VkSubmitInfo submitInfo = VkSubmitInfo.calloc();
        submitInfo.sType(VK_STRUCTURE_TYPE_SUBMIT_INFO);

        LongBuffer waitSemaphores = memAllocLong(1);
        IntBuffer waitStages = memAllocInt(1);
        waitSemaphores.put(0, imageAvailableSemaphore);
        waitStages.put(0, VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT);

        submitInfo.waitSemaphoreCount(1);
        submitInfo.pWaitSemaphores(waitSemaphores);
        submitInfo.pWaitDstStageMask(waitStages);

        PointerBuffer pCommandBuffer = memAllocPointer(1);
        pCommandBuffer.put(0, commandBuffer);
        submitInfo.pCommandBuffers(pCommandBuffer);

        LongBuffer signalSemaphores = memAllocLong(1);
        signalSemaphores.put(0, renderFinishedSemaphore);
        submitInfo.pSignalSemaphores(signalSemaphores);

        result = vkQueueSubmit(graphicsQueue, submitInfo, inFlightFence);
        checkError(result);

        VkPresentInfoKHR presentInfo = VkPresentInfoKHR.calloc();
        presentInfo.sType(VK_STRUCTURE_TYPE_PRESENT_INFO_KHR);
        presentInfo.pWaitSemaphores(signalSemaphores);

        LongBuffer swapChains = memAllocLong(1);
        swapChains.put(0, swapChain);
        presentInfo.swapchainCount(1);
        presentInfo.pSwapchains(swapChains);

        IntBuffer pImageIndices = memAllocInt(1);
        pImageIndices.put(0, pImageIndex[0]);
        presentInfo.pImageIndices(pImageIndices);
        presentInfo.pResults(null);

        vkQueuePresentKHR(presentQueue, presentInfo);

        memFree(signalSemaphores);
        memFree(pCommandBuffer);
        memFree(waitStages);
        memFree(waitSemaphores);
        submitInfo.free();
        memFree(pImageIndices);
        memFree(swapChains);
        presentInfo.free();
    }

    private void cleanUpSwapChain() {
        for (long framebuffer : swapChainFramebuffers) {
            vkDestroyFramebuffer(device, framebuffer, null);
        }

        for (long imageView : swapChainImageViews) {
            vkDestroyImageView(device, imageView, null);
        }

        vkDestroySwapchainKHR(device, swapChain, null);
    }

    private void recreateSwapChain() {
        vkDeviceWaitIdle(device);

        cleanUpSwapChain();

        createSwapChain();
        createImageViews();
        createFramebuffers();
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            drawFrame();
        }

        vkDeviceWaitIdle(device);
    }

    private void cleanUp() {
        cleanUpSwapChain();

        vkDestroyBuffer(device, vertexBuffer, null);
        vkFreeMemory(device, vertexBufferMemory, null);

        vkDestroySemaphore(device, imageAvailableSemaphore, null);
        vkDestroySemaphore(device, renderFinishedSemaphore, null);
        vkDestroyFence(device, inFlightFence, null);
        vkDestroyCommandPool(device, commandPool, null);
        vkDestroyPipeline(device, graphicsPipeline, null);
        vkDestroyPipelineLayout(device, pipelineLayout, null);
        vkDestroyRenderPass(device, renderPass, null);
        vkDestroyDevice(device, null);
        vkDestroySurfaceKHR(instance, surface, null);
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
