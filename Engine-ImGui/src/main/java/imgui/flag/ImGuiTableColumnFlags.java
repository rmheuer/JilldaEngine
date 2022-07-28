package imgui.flag;

public final class ImGuiTableColumnFlags {
    public static final int None = 0;
    public static final int Disabled = 1;
    public static final int DefaultHide = 1 << 1;
    public static final int DefaultSort = 1 << 2;
    public static final int WidthStretch = 1 << 3;
    public static final int WidthFixed = 1 << 4;
    public static final int NoResize = 1 << 5;
    public static final int NoReorder = 1 << 6;
    public static final int NoHide = 1 << 7;
    public static final int NoClip = 1 << 8;
    public static final int NoSort = 1 << 9;
    public static final int NoSortAscending = 1 << 10;
    public static final int NoSortDescending = 1 << 11;
    public static final int NoHeaderLabel = 1 << 12;
    public static final int NoHeaderWidth = 1 << 13;
    public static final int PreferSortAscending = 1 << 14;
    public static final int PreferSortDescending = 1 << 15;
    public static final int IndentEnable = 1 << 16;
    public static final int IndentDisable = 1 << 17;

    public static final int IsEnabled = 1 << 24;
    public static final int IsVisible = 1 << 25;
    public static final int IsSorted = 1 << 26;
    public static final int IsHovered = 1 << 27;

    public static final int WidthMask_ = WidthStretch | WidthFixed;
    public static final int IndentMask_ = IndentEnable | IndentDisable;
    public static final int StatusMask_ = IsEnabled | IsVisible | IsSorted | IsHovered;
    public static final int NoDirectResize_ = 1 << 30;

    private ImGuiTableColumnFlags() {
        throw new AssertionError();
    }
}
