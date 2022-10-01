package imgui.internal.flag;

public final class ImGuiItemStatusFlags {
    public static final int None = 0;
    public static final int HoveredRect = 1;
    public static final int HasDisplayRect = 1 << 1;
    public static final int Edited = 1 << 2;
    public static final int ToggledSelection = 1 << 3;
    public static final int ToggledOpen = 1 << 4;
    public static final int HasDeactivated = 1 << 5;
    public static final int Deactivated = 1 << 6;
    public static final int HoveredWindow = 1 << 7;
    public static final int FocusedByTabbing = 1 << 8;

    private ImGuiItemStatusFlags() {
        throw new AssertionError();
    }
}
