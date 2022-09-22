package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface RendererDestroyWindowFn {
    void call(ImGuiViewport/*ptr*/ vp);
}
