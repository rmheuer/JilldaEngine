package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformSetWindowFocusFn {
    void call(ImGuiViewport/*ptr*/ vp);
}
