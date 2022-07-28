package imgui.flag;

public final class ImGuiTreeNodeFlags {
    public static final int None = 0;
    public static final int Selected = 1;
    public static final int Framed = 1 << 1;
    public static final int AllowItemOverlap = 1 << 2;
    public static final int NoTreePushOnOpen = 1 << 3;
    public static final int NoAutoOpenOnLog = 1 << 4;
    public static final int DefaultOpen = 1 << 5;
    public static final int OpenOnDoubleClick = 1 << 6;
    public static final int OpenOnArrow = 1 << 7;
    public static final int Leaf = 1 << 8;
    public static final int Bullet = 1 << 9;
    public static final int FramePadding = 1 << 10;
    public static final int SpanAvailWidth = 1 << 11;
    public static final int SpanFullWidth = 1 << 12;
    public static final int NavLeftJumpsBackHere = 1 << 13;

    public static final int CollapsingHeader = Framed | NoTreePushOnOpen | NoAutoOpenOnLog;

    private ImGuiTreeNodeFlags() {
        throw new AssertionError();
    }
}
