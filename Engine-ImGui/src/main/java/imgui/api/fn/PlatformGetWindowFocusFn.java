package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformGetWindowFocusFn {
    boolean call(ImGuiViewport/*ptr*/ vp);
}
