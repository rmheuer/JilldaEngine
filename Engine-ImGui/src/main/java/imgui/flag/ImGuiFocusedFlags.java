package imgui.flag;

public final class ImGuiFocusedFlags {
    public static final int None = 0;
    public static final int ChildWindows = 1;
    public static final int RootWindow = 1 << 1;
    public static final int AnyWindow = 1 << 2;
    public static final int NoPopupHierarchy = 1 << 3;
    public static final int DockHierarchy = 1 << 4;

    public static final int RootAndChildWindows = RootWindow | ChildWindows;

    private ImGuiFocusedFlags() {
        throw new AssertionError();
    }
}
