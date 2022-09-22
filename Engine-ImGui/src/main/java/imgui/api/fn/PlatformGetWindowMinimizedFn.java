package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformGetWindowMinimizedFn {
    boolean call(ImGuiViewport/*ptr*/ vp);
}
