package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface RendererRenderWindowFn {
    void call(ImGuiViewport/*ptr*/ vp, Object renderArg);
}
