package imgui.api.flag;

public final class ImGuiWindowFlags {
    public static final int None = 0;
    public static final int NoTitleBar = 1;
    public static final int NoResize = 1 << 1;
    public static final int NoMove = 1 << 2;
    public static final int NoScrollbar = 1 << 3;
    public static final int NoScrollWithMouse = 1 << 4;
    public static final int NoCollapse = 1 << 5;
    public static final int AlwaysAutoResize = 1 << 6;
    public static final int NoBackground = 1 << 7;
    public static final int NoSavedSettings = 1 << 8;
    public static final int NoMouseInputs = 1 << 9;
    public static final int MenuBar = 1 << 10;
    public static final int HorizontalScrollbar = 1 << 11;
    public static final int NoFocusOnAppearing = 1 << 12;
    public static final int NoBringToFrontOnFocus = 1 << 13;
    public static final int AlwaysVerticalScrollbar = 1 << 14;
    public static final int AlwaysHorizontalScrollbar = 1 << 15;
    public static final int AlwaysUseWindowPadding = 1 << 16;
    public static final int NoNavInputs = 1 << 18;
    public static final int NoNavFocus = 1 << 19;
    public static final int UnsavedDocument = 1 << 20;
    public static final int NoDocking = 1 << 21;

    public static final int NoNav = NoNavInputs | NoNavFocus;
    public static final int NoDecoration = NoTitleBar | NoResize | NoScrollbar | NoCollapse;
    public static final int NoInputs = NoMouseInputs | NoNavInputs | NoNavFocus;

    // [Internal]
    public static final int NavFlattened = 1 << 23;
    public static final int ChildWindow = 1 << 24;
    public static final int Tooltip = 1 << 25;
    public static final int Popup = 1 << 26;
    public static final int Modal = 1 << 27;
    public static final int ChildMenu = 1 << 28;
    public static final int DockNodeHost = 1 << 29;

    private ImGuiWindowFlags() {
        throw new AssertionError();
    }
}
