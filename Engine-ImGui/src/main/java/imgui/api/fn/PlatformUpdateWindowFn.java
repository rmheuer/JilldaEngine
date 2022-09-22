package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformUpdateWindowFn {
    void call(ImGuiViewport/*ptr*/ vp);
}
