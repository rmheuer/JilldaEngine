package com.github.rmheuer.vulkantest;

import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;


public final class SwapChainSupportInfo {
    public VkSurfaceCapabilitiesKHR capabilities;
    public VkSurfaceFormatKHR.Buffer formats;
    public int[] presentModes;

    public void free() {
        capabilities.free();
        formats.free();
    }
}
