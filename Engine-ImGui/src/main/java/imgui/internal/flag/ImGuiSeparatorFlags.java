package imgui.internal.flag;

public final class ImGuiSeparatorFlags {
    public static final int None = 0;
    public static final int Horizontal = 1;
    public static final int Vertical = 1 << 1;
    public static final int SpanAllColumns = 1 << 2;

    private ImGuiSeparatorFlags() {
        throw new AssertionError();
    }
}
