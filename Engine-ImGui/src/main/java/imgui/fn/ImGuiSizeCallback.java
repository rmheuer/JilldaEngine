package imgui.fn;

@FunctionalInterface
public interface ImGuiSizeCallback {
    void call(ImGuiSizeCallbackData data);
}
