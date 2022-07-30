package imgui.fn;

@FunctionalInterface
public interface ImGuiSetClipboardTextFn {
    void set(Object userData, String text);
}
