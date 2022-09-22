package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformShowWindowFn {
    void call(ImGuiViewport/*ptr*/ vp);
}
