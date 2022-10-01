package imgui.internal.flag;

public final class ImGuiItemFlags {
    public static final int None = 0;
    public static final int NoTabStop = 1;
    public static final int ButtonRepeat = 1 << 1;
    public static final int Disabled = 1 << 2;
    public static final int NoNav = 1 << 3;
    public static final int NoNavDefaultFocus = 1 << 4;
    public static final int SelectableDontClosePopup = 1 << 5;
    public static final int MixedValue = 1 << 6;
    public static final int ReadOnly = 1 << 7;

    public static final int Inputable = 1 << 8;

    private ImGuiItemFlags() {
        throw new AssertionError();
    }
}
