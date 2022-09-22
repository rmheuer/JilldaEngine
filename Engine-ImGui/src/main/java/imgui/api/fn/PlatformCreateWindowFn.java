package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformCreateWindowFn {
    void call(ImGuiViewport/*ptr*/ vp);
}
