package com.github.rmheuer.engine.render.vulkan;

import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.render.WindowSettings;
import com.github.rmheuer.engine.render.glfw.GLFWWindow;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.glfwCreateWindowSurface;
import static org.lwjgl.vulkan.KHRSurface.*;
import static org.lwjgl.vulkan.KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR;
import static org.lwjgl.vulkan.KHRSwapchain.*;
import static org.lwjgl.vulkan.KHRSwapchain.vkGetSwapchainImagesKHR;
import static org.lwjgl.vulkan.VK10.*;

public final class VulkanWindow extends GLFWWindow {
    private static final String[] DEVICE_EXTENSION_NAMES = {
            VK_KHR_SWAPCHAIN_EXTENSION_NAME
    };

    private final VulkanContext context;
    private final VkInstance instance;
    private long windowHandle;

    private long surface;

    private VkPhysicalDevice physicalDevice;
    private VkDevice device;
    private QueueFamilyIndices queueIndices;
    private VkQueue graphicsQueue, presentQueue;

    private long swapChain;
    private int swapChainImageFormat;
    private VkExtent2D swapChainExtent;
    private final List<Long> swapChainImages;
    private final List<Long> swapChainImageViews;

    public VulkanWindow(VulkanContext context, WindowSettings settings) {
        super(settings);
        this.context = context;
        this.instance = context.getInstance();
        swapChainImages = new ArrayList<>();
        swapChainImageViews = new ArrayList<>();

        createSurface(windowHandle);
        pickPhysicalDevice();
        createLogicalDevice();
        createSwapChain();
    }

    @Override
    protected void setContextWindowHints() {
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
    }

    /**
     * Creates the window surface to render onto
     */
    private void createSurface(long window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Use GLFW to create a surface for the window
            LongBuffer pSurface = stack.mallocLong(1);
            if (glfwCreateWindowSurface(instance, window, null, pSurface) != VK_SUCCESS) {
                throw new RuntimeException("Failed to create window surface");
            }
            surface = pSurface.get(0);
        }
    }

    /**
     * Finds the indices of the queue families we need from the device.
     *
     * @param device device to get queue families for
     * @return indices of the queue families
     */
    private QueueFamilyIndices findQueueFamilies(VkPhysicalDevice device) {
        QueueFamilyIndices indices = new QueueFamilyIndices();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Get the number of queue families the device has
            IntBuffer pQueueFamilyCount = stack.mallocInt(1);
            vkGetPhysicalDeviceQueueFamilyProperties(device, pQueueFamilyCount, null);
            int queueFamilyCount = pQueueFamilyCount.get(0);

            // Get the properties of all the queue families of the device
            VkQueueFamilyProperties.Buffer queueFamilies = VkQueueFamilyProperties.malloc(queueFamilyCount);
            vkGetPhysicalDeviceQueueFamilyProperties(device, pQueueFamilyCount, queueFamilies);

            IntBuffer pSupported = stack.mallocInt(1);
            for (int i = 0; i < queueFamilyCount; i++) {
                // Check if the current queue family has the properties we need, and
                // if it does, store its index
                VkQueueFamilyProperties queueFamily = queueFamilies.get(i);
                if ((queueFamily.queueFlags() & VK_QUEUE_GRAPHICS_BIT) != 0)
                    indices.graphicsFamily = i;

                vkGetPhysicalDeviceSurfaceSupportKHR(device, i, surface, pSupported);
                if (pSupported.get(0) != 0)
                    indices.presentFamily = i;

                // If we have found all the queue families we need, we can stop searching
                if (indices.isComplete())
                    break;
            }
        }

        return indices;
    }

    /**
     * Checks if a device supports all of the required extensions.
     *
     * @param device device to check
     * @return whether the device supports all the required extensions
     */
    private boolean checkDeviceExtensionSupport(VkPhysicalDevice device) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Get the number of available device extensions
            IntBuffer pExtensionCount = stack.mallocInt(1);
            vkEnumerateDeviceExtensionProperties(device, (ByteBuffer) null, pExtensionCount, null);

            // Get the properties of all the available extensions
            VkExtensionProperties.Buffer availableExtensions = VkExtensionProperties.malloc(pExtensionCount.get(0), stack);
            vkEnumerateDeviceExtensionProperties(device, (ByteBuffer) null, pExtensionCount, availableExtensions);

            // Collect the names of the extensions into a set to eliminate
            // duplicates
            Set<String> availExtensions = new HashSet<>();
            for (VkExtensionProperties extension : availableExtensions)
                availExtensions.add(extension.extensionNameString());

            // Make sure all the required extensions are present
            return availExtensions.containsAll(Arrays.asList(DEVICE_EXTENSION_NAMES));
        }
    }

    /**
     * Gets the available swap chain properties for a physical device.
     *
     * @param device device to check
     * @param stack stack to allocate Vulkan structs on
     * @return support details
     */
    private SwapChainSupportDetails querySwapChainSupport(VkPhysicalDevice device, MemoryStack stack) {
        SwapChainSupportDetails details = new SwapChainSupportDetails();

        // Get the capabilities of the physical device in relation to the surface
        details.capabilities = VkSurfaceCapabilitiesKHR.malloc(stack);
        vkGetPhysicalDeviceSurfaceCapabilitiesKHR(device, surface, details.capabilities);

        // Get the available surface formats the device supports
        IntBuffer pFormatCount = stack.mallocInt(1);
        vkGetPhysicalDeviceSurfaceFormatsKHR(device, surface, pFormatCount, null);
        int formatCount = pFormatCount.get(0);
        if (formatCount != 0) {
            details.formats = VkSurfaceFormatKHR.malloc(formatCount, stack);
            vkGetPhysicalDeviceSurfaceFormatsKHR(device, surface, pFormatCount, details.formats);
        }

        // Get the available presentation modes the device supports
        IntBuffer pPresentModeCount = stack.mallocInt(1);
        vkGetPhysicalDeviceSurfacePresentModesKHR(device, surface, pPresentModeCount, null);
        int presentModeCount = pPresentModeCount.get(0);
        if (presentModeCount != 0) {
            details.presentModes = stack.mallocInt(presentModeCount);
            vkGetPhysicalDeviceSurfacePresentModesKHR(device, surface, pPresentModeCount, details.presentModes);
        }

        return details;
    }

    /**
     * Calculates a score for a physical device. A higher score means
     * a higher priority for using the device, and a negative score means
     * that the device cannot be used.
     *
     * @return score for the device
     */
    private int scorePhysicalDevice(VkPhysicalDevice device, QueueFamilyIndices queueFamilyIndices) {
        // Make sure the device supports all the required queues and device
        // specific extensions
        if (!queueFamilyIndices.isComplete())
            return -1;
        if (!checkDeviceExtensionSupport(device))
            return -1;

        int score = 0;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Ensure the device supports a swap chain
            SwapChainSupportDetails swapChainSupport = querySwapChainSupport(device, stack);
            if (swapChainSupport.formats == null || swapChainSupport.presentModes == null)
                return -1;

            // Get the properties of this device
            VkPhysicalDeviceProperties deviceProperties = VkPhysicalDeviceProperties.malloc(stack);
            vkGetPhysicalDeviceProperties(device, deviceProperties);

            // Get the features this device supports
            VkPhysicalDeviceFeatures deviceFeatures = VkPhysicalDeviceFeatures.malloc(stack);
            vkGetPhysicalDeviceFeatures(device, deviceFeatures);

            // Print out the name of the device
            System.out.print("    " + deviceProperties.deviceNameString());

            // Heavily prioritize discrete (dedicated) GPUs, and somewhat
            // prioritize integrated GPUs
            switch (deviceProperties.deviceType()) {
                case VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU:
                    score += 1000;
                    break;
                case VK_PHYSICAL_DEVICE_TYPE_INTEGRATED_GPU:
                    score += 500;
                    break;
            }

            // Prioritize GPUs that can present and render on the same queue
            if (queueFamilyIndices.presentFamily.equals(queueFamilyIndices.graphicsFamily))
                score += 100;
        }
        System.out.print(" (score " + score);
        return score;
    }

    /**
     * Selects a physical device (GPU) that is present in the system to
     * use for rendering.
     */
    private void pickPhysicalDevice() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Get the number of GPUs present that support Vulkan
            IntBuffer pDeviceCount = stack.mallocInt(1);
            vkEnumeratePhysicalDevices(instance, pDeviceCount, null);
            int deviceCount = pDeviceCount.get(0);

            // Make sure there is at least one GPU that supports Vulkan present
            if (deviceCount == 0)
                throw new RuntimeException("Failed to find a GPU that supports Vulkan");

            // Get all the available devices
            PointerBuffer pDevices = stack.mallocPointer(deviceCount);
            vkEnumeratePhysicalDevices(instance, pDeviceCount, pDevices);

            // Find the best scoring device
            int bestScoreSoFar = Integer.MIN_VALUE;
            System.out.println("Available physical devices:");
            for (int i = 0; i < deviceCount; i++) {
                VkPhysicalDevice device = new VkPhysicalDevice(pDevices.get(i), instance);
                QueueFamilyIndices queueFamilyIndices = findQueueFamilies(device);
                int score = scorePhysicalDevice(device, queueFamilyIndices);

                // If this device scores higher than the current one, switch to it
                if (score > bestScoreSoFar) {
                    physicalDevice = device;
                    queueIndices = queueFamilyIndices;
                    bestScoreSoFar = score;

                    System.out.println(", chosen)");
                } else {
                    System.out.println(")");
                }
            }

            // Make sure we found a GPU
            if (physicalDevice == null || bestScoreSoFar < 0)
                throw new RuntimeException("Failed to find a suitable GPU");
        }
    }

    /**
     * Creates the logical device to use for rendering with the physical
     * device. The logical device is necessary because it specifies which
     * features we want to be able to use.
     */
    private void createLogicalDevice() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            Set<Integer> uniqueQueueFamilies = new HashSet<>();
            uniqueQueueFamilies.add(queueIndices.graphicsFamily);
            uniqueQueueFamilies.add(queueIndices.presentFamily);

            // Set the priorities of the queue to 1
            // Priority can be from 0 to 1
            FloatBuffer pQueuePriority = stack.floats(1.0f);

            // Create the graphics queue using the graphics family index
            VkDeviceQueueCreateInfo.Buffer queueCreateInfos = VkDeviceQueueCreateInfo.calloc(uniqueQueueFamilies.size(), stack);
            int queueIdx = 0;
            for (int family : uniqueQueueFamilies) {
                VkDeviceQueueCreateInfo queueCreateInfo = queueCreateInfos.get(queueIdx);
                queueCreateInfo.sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO);
                queueCreateInfo.queueFamilyIndex(family);
                queueCreateInfo.pQueuePriorities(pQueuePriority);
                queueIdx++;
            }

            // Configure the device features we want to use. There currently
            // aren't any special features to enable
            VkPhysicalDeviceFeatures deviceFeatures = VkPhysicalDeviceFeatures.calloc(stack);

            // Pack all of the parameters into a create info struct
            VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.calloc(stack);
            createInfo.sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO);
            createInfo.pQueueCreateInfos(queueCreateInfos);
            createInfo.pEnabledFeatures(deviceFeatures);

            // Enable the required device-specific extensions
            PointerBuffer ppEnabledExtensionNames = stack.mallocPointer(DEVICE_EXTENSION_NAMES.length);
            for (String name : DEVICE_EXTENSION_NAMES)
                ppEnabledExtensionNames.put(stack.ASCII(name));
            ppEnabledExtensionNames.flip();
            createInfo.ppEnabledExtensionNames(ppEnabledExtensionNames);

            // If validation layers are enabled, we need to tell Vulkan that we want
            // to use them. The device-specific layers are ignored by modern versions
            // of Vulkan, but we still set them to be compatible with older versions.
            if (context.isValidationLayersEnabled()) {
                // Store all the validation layer names into a PointerBuffer
                PointerBuffer pLayerNames = stack.mallocPointer(VulkanContext.VALIDATION_LAYER_NAMES.length);
                for (String name : VulkanContext.VALIDATION_LAYER_NAMES) {
                    pLayerNames.put(stack.ASCII(name));
                }

                // Give the layer names to the create info struct
                createInfo.ppEnabledLayerNames(pLayerNames);
            }

            // Create the logical device
            PointerBuffer pDevice = stack.mallocPointer(1);
            if (vkCreateDevice(physicalDevice, createInfo, null, pDevice) != VK_SUCCESS) {
                throw new RuntimeException("Failed to create logical device");
            }
            device = new VkDevice(pDevice.get(0), physicalDevice, createInfo);

            // Get the handles to the queues we requested
            PointerBuffer pQueue = stack.mallocPointer(1);
            vkGetDeviceQueue(device, queueIndices.graphicsFamily, 0, pQueue);
            graphicsQueue = new VkQueue(pQueue.get(0), device);
            vkGetDeviceQueue(device, queueIndices.presentFamily, 0, pQueue);
            presentQueue = new VkQueue(pQueue.get(0), device);
        }
    }

    /**
     * Selects the most optimal surface format available.
     *
     * @param availableFormats all available formats on the device
     * @return the most optimal format
     */
    private VkSurfaceFormatKHR chooseSwapSurfaceFormat(VkSurfaceFormatKHR.Buffer availableFormats) {
        for (VkSurfaceFormatKHR format : availableFormats) {
            System.out.println("Available format: " + format.format() + ", colorspace: " + format.colorSpace());

            // Prefer a format that stores each color component as an 8-bit
            // value by default
            if (format.format() == VK_FORMAT_B8G8R8A8_UNORM && format.colorSpace() == VK_COLOR_SPACE_SRGB_NONLINEAR_KHR) {
                System.out.println("Found preferred surface format");
                return format;
            }
        }

        // If we couldn't find a preferred format, use the first one available
        System.out.println("Did not find preferred surface format, defaulting to first available surface format");
        return availableFormats.get(0);
    }

    /**
     * Choose the optimal presentation mode for the swapchain.
     *
     * @param availablePresentModes presentation modes supported by the device
     * @return optimal presentation mode
     */
    private int chooseSwapPresentMode(IntBuffer availablePresentModes) {
        // If mailbox mode is available, use it. Mailbox mode means that we
        // can continue to render frames faster than the display's refresh
        // rate, since it replaces older images with newer ones if they are
        // available.
        for (int i = 0; i < availablePresentModes.capacity(); i++) {
            if (availablePresentModes.get(i) == VK_PRESENT_MODE_MAILBOX_KHR)
                return VK_PRESENT_MODE_MAILBOX_KHR;
        }

        // If mailbox mode isn't supported, default to FIFO mode, which is
        // guaranteed to be supported. This means that every frame rendered
        // will be shown to the display in order.
        return VK_PRESENT_MODE_FIFO_KHR;
    }

    /**
     * Selects the best size for the swap chain based on the size of the window
     *
     * @param capabilities capabilities of the surface
     * @param stack stack to allocate the extent with
     * @return best size. Must be freed manually
     */
    private VkExtent2D chooseSwapExtent(VkSurfaceCapabilitiesKHR capabilities, MemoryStack stack) {
        if (capabilities.currentExtent().width() != 0xFFFFFFFF) {
            // If Vulkan has already provided an optimal extent, use it

            // Create a new extent so that we own the instance
            VkExtent2D current = capabilities.currentExtent();
            VkExtent2D extent = VkExtent2D.calloc();
            extent.set(current);

            return extent;
        } else {
            // Otherwise, choose the extent that is the same size as the
            // window's framebuffer

            // Get the size of the framebuffer
            Vector2i framebufferSize = getFramebufferSize();

            // Get the limits for how big or small the swapchain can be
            VkExtent2D min = capabilities.minImageExtent();
            VkExtent2D max = capabilities.maxImageExtent();

            // Choose the best size within the limits
            int w = MathUtils.clamp(framebufferSize.x, min.width(), max.width());
            int h = MathUtils.clamp(framebufferSize.y, min.height(), max.height());

            // Store the size in a VkExtent2D struct
            VkExtent2D actualExtent = VkExtent2D.calloc();
            actualExtent.set(w, h);
            return actualExtent;
        }
    }

    /**
     * Creates the swap chain to present images to the window. The swap chain
     * is essentially a queue of images to present to the screen. The
     * application draws to an image, and then submits it to the queue
     * to be presented later.
     */
    private void createSwapChain() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            SwapChainSupportDetails support = querySwapChainSupport(physicalDevice, stack);

            // Choose the format, mode, and extent that we want to create
            // the swapchain
            VkSurfaceFormatKHR surfaceFormat = chooseSwapSurfaceFormat(support.formats);
            int presentMode = chooseSwapPresentMode(support.presentModes);
            VkExtent2D extent = chooseSwapExtent(support.capabilities, stack);

            // Store the format and extent for later
            swapChainImageFormat = surfaceFormat.format();
            swapChainExtent = extent;

            // Get the number of images to put into the queue. We want one more
            // than the minimum to reduce the chance that we need to wait on
            // the Vulkan driver to acquire an image
            int imageCount = support.capabilities.minImageCount() + 1;

            // Make sure we aren't requesting more images than is supported
            int maxImageCount = support.capabilities.maxImageCount();
            if (maxImageCount > 0 && imageCount > maxImageCount)
                imageCount = maxImageCount;

            // Set up the creation info struct
            VkSwapchainCreateInfoKHR createInfo = VkSwapchainCreateInfoKHR.calloc(stack);
            createInfo.sType(VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR);
            createInfo.surface(surface);
            createInfo.minImageCount(imageCount);
            createInfo.imageFormat(surfaceFormat.format());
            createInfo.imageColorSpace(surfaceFormat.colorSpace());
            createInfo.imageExtent(extent);
            createInfo.imageArrayLayers(1); // We only have one image layer at the moment
            createInfo.imageUsage(VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT);

            // Get the queue family indices for rendering and presentation
            int graphicsFamily = queueIndices.graphicsFamily;
            int presentFamily = queueIndices.presentFamily;

            // Configure the sharing mode for the images
            if (graphicsFamily != presentFamily) {
                IntBuffer queueIndices = stack.ints(graphicsFamily, presentFamily);

                // We are rendering and presenting on different queues, so we
                // need to ensure the images can be used by both simultaneously
                createInfo.imageSharingMode(VK_SHARING_MODE_CONCURRENT);
                createInfo.pQueueFamilyIndices(queueIndices);
            } else {
                // If they are the same queue, the images can be exclusive to
                // that queue
                createInfo.imageSharingMode(VK_SHARING_MODE_EXCLUSIVE);
                createInfo.queueFamilyIndexCount(0);
            }

            // We don't need to apply a transform to the images, so we can just
            // leave the transform as it is
            createInfo.preTransform(support.capabilities.currentTransform());

            // Make the window opaque, so we don't need to blend with any
            // other windows behind it
            createInfo.compositeAlpha(VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR);

            createInfo.presentMode(presentMode);

            // We don't need to bother rendering any pixels that are covered by
            // another window, since we won't be reading their values back
            createInfo.clipped(true);

            // There was not any previous swapchain
            createInfo.oldSwapchain(VK_NULL_HANDLE);

            // Create the swapchain
            LongBuffer pSwapChain = stack.mallocLong(1);
            if (vkCreateSwapchainKHR(device, createInfo, null, pSwapChain) != VK_SUCCESS) {
                throw new RuntimeException("Failed to create swap chain");
            }
            swapChain = pSwapChain.get(0);

            // Get the handles to the swapchain images
            IntBuffer pImageCount = stack.mallocInt(1);
            vkGetSwapchainImagesKHR(device, swapChain, pImageCount, null);
            LongBuffer pImages = stack.mallocLong(pImageCount.get(0));
            vkGetSwapchainImagesKHR(device, swapChain, pImageCount, pImages);

            // Store the swapchain image handles into a list
            for (int i = 0; i < pImages.capacity(); i++)
                swapChainImages.add(pImages.get(i));
        }
    }

    /**
     * Creates the views onto the swapchain images.
     */
    private void createImageViews() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Pre-allocate the create info struct. The same struct instance
            // will be reused for each iteration of the loop.
            VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.calloc(stack);

            // Pre-allocate a buffer to get the image view handles
            LongBuffer pView = stack.mallocLong(1);

            // Create the image view for each swapchain image
            for (long image : swapChainImages) {
                createInfo.sType(VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO);
                createInfo.image(image);

                // This is a two-dimensional image
                createInfo.viewType(VK_IMAGE_VIEW_TYPE_2D);

                // We want to access the images in the same format they are stored in
                createInfo.format(swapChainImageFormat);

                // All of the image components should map to themselves
                VkComponentMapping components = createInfo.components();
                components.r(VK_COMPONENT_SWIZZLE_IDENTITY);
                components.g(VK_COMPONENT_SWIZZLE_IDENTITY);
                components.b(VK_COMPONENT_SWIZZLE_IDENTITY);
                components.a(VK_COMPONENT_SWIZZLE_IDENTITY);

                // This image has one layer, is used as a color target, and
                // does not have any mipmap levels.
                VkImageSubresourceRange subresourceRange = createInfo.subresourceRange();
                subresourceRange.aspectMask(VK_IMAGE_ASPECT_COLOR_BIT);
                subresourceRange.baseMipLevel(0);
                subresourceRange.levelCount(1);
                subresourceRange.baseArrayLayer(0);
                subresourceRange.layerCount(1);

                if (vkCreateImageView(device, createInfo, null, pView) != VK_SUCCESS) {
                    throw new RuntimeException("Failed to create image views");
                }
                swapChainImageViews.add(pView.get(0));
            }
        }
    }

    @Override
    protected void initContext(long handle) {
        windowHandle = handle;
    }

    /**
     * Disposes of the current swapchain, image views, and framebuffers.
     */
    private void cleanUpSwapChain() {
        // Delete the framebuffers
//        for (long framebuffer : swapChainFramebuffers)
//            vkDestroyFramebuffer(device, framebuffer, null);

        // Delete the image views
        for (long imageView : swapChainImageViews)
            vkDestroyImageView(device, imageView, null);

        // Delete the swapchain
        vkDestroySwapchainKHR(device, swapChain, null);

        // Free the extent struct since we don't need it anymore; it will be
        // replaced by a new one when recreating the swapchain
        swapChainExtent.free();
    }

    @Override
    public void close() {
        cleanUpSwapChain();

        vkDestroyDevice(device, null);
        vkDestroySurfaceKHR(instance, surface, null);
    }
}
