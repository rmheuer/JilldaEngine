package imgui.api.flag;

public final class ImGuiCond {
    public static final int None = 0;
    public static final int Always = 1;
    public static final int Once = 1 << 1;
    public static final int FirstUseEver = 1 << 2;
    public static final int Appearing = 1 << 3;

    private ImGuiCond() {
        throw new AssertionError();
    }
}
