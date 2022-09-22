package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface PlatformSetWindowAlphaFn {
    void call(ImGuiViewport/*ptr*/ vp, float alpha);
}
