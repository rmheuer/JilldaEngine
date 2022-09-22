package imgui.api.flag;

public final class ImGuiSelectableFlags {
    public static final int None = 0;
    public static final int DontClosePopups = 1;
    public static final int SpanAllColumns = 1 << 1;
    public static final int AllowDoubleClick = 1 << 2;
    public static final int Disabled = 1 << 3;
    public static final int AllowItemOverlap = 1 << 4;

    private ImGuiSelectableFlags() {
        throw new AssertionError();
    }
}
