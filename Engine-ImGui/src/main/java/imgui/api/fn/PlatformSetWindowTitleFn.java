package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformSetWindowTitleFn {
    void call(ImGuiViewport/*ptr*/ vp, String str);
}
