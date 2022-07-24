package imgui;

@FunctionalInterface
public interface ImGuiItemsGetter {
    boolean get(Object data, int idx, String[][] outText);
}
