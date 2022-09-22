package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface RendererSwapBuffersFn {
    void call(ImGuiViewport/*ptr*/ vp, Object renderArg);
}
