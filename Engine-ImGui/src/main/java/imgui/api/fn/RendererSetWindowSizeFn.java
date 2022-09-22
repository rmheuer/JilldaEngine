package imgui.api.fn;

import imgui.api.ImGuiViewport;
import imgui.api.ImVec2;

@FunctionalInterface
public interface RendererSetWindowSizeFn {
    void call(ImGuiViewport/*ptr*/ vp, ImVec2 size);
}
