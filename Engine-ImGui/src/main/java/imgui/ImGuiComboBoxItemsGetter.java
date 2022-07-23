package imgui;

@FunctionalInterface
public interface ImGuiComboBoxItemsGetter {
    boolean get(Object data, int idx, String[][] outText);
}
