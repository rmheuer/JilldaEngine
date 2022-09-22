package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformDestroyWindowFn {
    void call(ImGuiViewport/*ptr*/ vp);
}
