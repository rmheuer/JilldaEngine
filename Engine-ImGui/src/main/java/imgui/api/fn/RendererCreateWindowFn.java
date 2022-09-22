package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface RendererCreateWindowFn {
    void call(ImGuiViewport/*ptr*/ vp);
}
