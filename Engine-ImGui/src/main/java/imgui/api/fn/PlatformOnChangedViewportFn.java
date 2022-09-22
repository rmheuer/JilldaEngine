package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformOnChangedViewportFn {
    void call(ImGuiViewport/*ptr*/ vp);
}
