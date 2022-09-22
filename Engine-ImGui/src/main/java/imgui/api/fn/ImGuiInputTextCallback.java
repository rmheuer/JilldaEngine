package imgui.api.fn;

@FunctionalInterface
public interface ImGuiInputTextCallback {
    void call(ImGuiInputTextCallbackData data);
}
