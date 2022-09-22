package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformCreateVkSurfaceFn {
    int call(ImGuiViewport/*ptr*/ vp, long vkInst, Object vkAllocators, long[] outVkSurface);
}
