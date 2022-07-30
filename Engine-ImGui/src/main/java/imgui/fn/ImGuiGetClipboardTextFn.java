package imgui.fn;

@FunctionalInterface
public interface ImGuiGetClipboardTextFn {
    String get(Object userData);
}
