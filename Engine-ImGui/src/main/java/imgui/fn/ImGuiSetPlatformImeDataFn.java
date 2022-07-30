package imgui.fn;

import imgui.ImGuiViewport;

@FunctionalInterface
public interface ImGuiSetPlatformImeDataFn {
    void set(ImGuiViewport viewport, ImGuiPlatformImeData data);
}
