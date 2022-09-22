package imgui.api.flag;

public final class ImGuiViewportFlags {
    public static final int None = 0;
    public static final int IsPlatformWindow = 1;
    public static final int IsPlatformMonitor = 1 << 1;
    public static final int OwnedByApp = 1 << 2;
    public static final int NoDecoration = 1 << 3;
    public static final int NoTaskBarIcon = 1 << 4;
    public static final int NoFocusOnAppearing = 1 << 5;
    public static final int NoFocusOnClick = 1 << 6;
    public static final int NoInputs = 1 << 7;
    public static final int NoRendererClear = 1 << 8;
    public static final int TopMost = 1 << 9;
    public static final int Minimized = 1 << 10;
    public static final int NoAutoMerge = 1 << 11;
    public static final int CanHostOtherWindows = 1 << 12;

    private ImGuiViewportFlags() {
        throw new AssertionError();
    }
}
