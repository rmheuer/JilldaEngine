package imgui.api.fn;

@FunctionalInterface
public interface ImGuiGetClipboardTextFn {
    String get(Object userData);
}
