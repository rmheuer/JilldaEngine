package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformGetWindowDpiScaleFn {
    float call(ImGuiViewport/*ptr*/ vp);
}
