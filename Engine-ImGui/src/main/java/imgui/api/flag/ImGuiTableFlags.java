package imgui.api.flag;

public final class ImGuiTableFlags {
    public static final int None = 0;
    public static final int Resizable = 1;
    public static final int Reorderable = 1 << 1;
    public static final int Hideable = 1 << 2;
    public static final int Sortable = 1 << 3;
    public static final int NoSavedSettings = 1 << 4;
    public static final int ContextMenuInBody = 1 << 5;

    public static final int RowBg = 1 << 6;
    public static final int BordersInnerH = 1 << 7;
    public static final int BordersOuterH = 1 << 8;
    public static final int BordersInnerV = 1 << 9;
    public static final int BordersOuterV = 1 << 10;
    public static final int BordersH = BordersInnerH | BordersOuterH;
    public static final int BordersV = BordersInnerV | BordersOuterV;
    public static final int BordersInner = BordersInnerH | BordersInnerV;
    public static final int BordersOuter = BordersOuterH | BordersOuterV;
    public static final int Borders = BordersInner | BordersOuter;
    public static final int NoBordersInBody = 1 << 11;
    public static final int NoBordersInBodyUntilResize = 1 << 12;

    public static final int SizingFixedFit = 1 << 13;
    public static final int SizingFixedSame = 2 << 13;
    public static final int SizingStretchProp = 3 << 13;
    public static final int SizingStretchSame = 4 << 13;

    public static final int NoHostExtendX = 1 << 16;
    public static final int NoHostExtendY = 1 << 17;
    public static final int NoKeepColumnsVisible = 1 << 18;
    public static final int PreciseWidths = 1 << 19;

    public static final int NoClip = 1 << 20;

    public static final int PadOuterX = 1 << 21;
    public static final int NoPadOuterX = 1 << 22;
    public static final int NoPadInnerX = 1 << 23;

    public static final int ScrollX = 1 << 24;
    public static final int ScrollY = 1 << 25;

    public static final int SortMulti = 1 << 26;
    public static final int SortTristate = 1 << 27;

    public static final int SizingMask_ = SizingFixedFit | SizingFixedSame | SizingStretchProp | SizingStretchSame;

    private ImGuiTableFlags() {
        throw new AssertionError();
    }
}
