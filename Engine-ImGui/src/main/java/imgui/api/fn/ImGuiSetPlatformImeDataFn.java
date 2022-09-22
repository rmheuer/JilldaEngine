package imgui.api.fn;

import imgui.api.ImGuiViewport;

@FunctionalInterface
public interface ImGuiSetPlatformImeDataFn {
    void set(ImGuiViewport viewport, ImGuiPlatformImeData data);
}
