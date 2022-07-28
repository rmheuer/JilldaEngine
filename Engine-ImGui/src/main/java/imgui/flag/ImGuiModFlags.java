package imgui.flag;

public final class ImGuiModFlags {
    public static final int None = 0;
    public static final int Ctrl = 1;
    public static final int Shift = 1 << 1;
    public static final int Alt = 1 << 2;
    public static final int Super = 1 << 3;

    private ImGuiModFlags() {
        throw new AssertionError();
    }
}
