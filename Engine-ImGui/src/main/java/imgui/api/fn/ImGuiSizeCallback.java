package imgui.api.fn;

@FunctionalInterface
public interface ImGuiSizeCallback {
    void call(ImGuiSizeCallbackData data);
}
