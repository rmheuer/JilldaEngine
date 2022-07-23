package imgui;

@FunctionalInterface
public interface ImGuiSizeCallback {
    void call(ImGuiSizeCallbackData data);
}
