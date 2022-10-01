package imgui.internal.flag;

public final class ImGuiButtonFlagsPrivate {
    public static final int PressedOnClick = 1 << 4;
    public static final int PressedOnClickRelease = 1 << 5;
    public static final int PressedOnClickReleaseAnywhere = 1 << 6;
    public static final int PressedOnRelease = 1 << 7;
    public static final int PressedOnDoubleClick = 1 << 8;
    public static final int PressedOnDragDropHold = 1 << 9;
    public static final int Repeat = 1 << 10;
    public static final int FlattenChildren = 1 << 11;
    public static final int AllowItemOverlap = 1 << 12;
    public static final int DontClosePopups = 1 << 13;
    public static final int AlignTextBaseLine = 1 << 15;
    public static final int NoKeyModifiers = 1 << 16;
    public static final int NoHoldingActiveId = 1 << 17;
    public static final int NoNavFocus = 1 << 18;
    public static final int NoHoveredOnFocus = 1 << 19;

    public static final int PressedOnMask_ = PressedOnClick | PressedOnClickRelease | PressedOnClickReleaseAnywhere | PressedOnRelease | PressedOnDoubleClick | PressedOnDragDropHold;
    public static final int PressedOnDefault_ = PressedOnClickRelease;

    private ImGuiButtonFlagsPrivate() {
        throw new AssertionError();
    }
}
