package imgui.api.fn;

import imgui.api.ImGuiViewport;
import imgui.api.ImVec2;

@FunctionalInterface
public interface PlatformGetWindowPosFn {
    ImVec2 call(ImGuiViewport/*ptr*/ vp);
}