package imgui;

@FunctionalInterface
public interface ImGuiInputTextCallback {
    void call(ImGuiInputTextCallbackData data);
}
