package imgui.fn;

@FunctionalInterface
public interface ImGuiItemsGetter {
    boolean get(Object data, int idx, String[][] outText);
}
