package imgui.api.fn;

@FunctionalInterface
public interface ImGuiSetClipboardTextFn {
    void set(Object userData, String text);
}
