package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformSwapBuffersFn {
    void call(ImGuiViewport/*ptr*/ vp, Object renderArg);
}
