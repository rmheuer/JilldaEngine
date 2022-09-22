package imgui.api.flag;

public final class ImGuiHoveredFlags {
    public static final int None = 0;
    public static final int ChildWindows = 1;
    public static final int RootWindow = 1 << 1;
    public static final int AnyWindow = 1 << 2;
    public static final int NoPopupHierarchy = 1 << 3;
    public static final int DockHierarchy = 1 << 4;
    public static final int AllowWhenBlockedByPopup = 1 << 5;
    public static final int AllowWhenBlockedByActiveItem = 1 << 7;
    public static final int AllowWhenOverlapped = 1 << 8;
    public static final int AllowWhenDisabled = 1 << 9;
    public static final int NoNavOverride = 1 << 10;

    public static final int RectOnly = AllowWhenBlockedByPopup | AllowWhenBlockedByActiveItem | AllowWhenOverlapped;
    public static final int RootAndChildWindows = RootWindow | ChildWindows;

    public ImGuiHoveredFlags() {
        throw new AssertionError();
    }
}
