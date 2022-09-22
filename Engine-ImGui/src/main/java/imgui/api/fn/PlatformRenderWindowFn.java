package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformRenderWindowFn {
    void call(ImGuiViewport/*ptr*/ vp, Object renderArg);
}
