package imgui.fn;

@FunctionalInterface
public interface ImGuiInputTextCallback {
    void call(ImGuiInputTextCallbackData data);
}
