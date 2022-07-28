package imgui.flag;

public final class ImGuiTabItemFlags {
    public static final int None = 0;
    public static final int UnsavedDocument = 1;
    public static final int SetSelected = 1 << 1;
    public static final int NoCloseWithMiddleMouseButton = 1 << 2;
    public static final int NoPushId = 1 << 3;
    public static final int NoTooltip = 1 << 4;
    public static final int NoReorder = 1 << 5;
    public static final int Leading = 1 << 6;
    public static final int Trailing = 1 << 7;

    private ImGuiTabItemFlags() {
        throw new AssertionError();
    }
}
