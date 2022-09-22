package imgui.api.flag;

public final class ImGuiSliderFlags {
    public static final int None = 0;
    public static final int AlwaysClamp = 1 << 4;
    public static final int Logarithmic = 1 << 5;
    public static final int NoRoundToFormat = 1 << 6;
    public static final int NoInput = 1 << 7;

    public static final int InvalidMask_ = 0x7000000F;

    public ImGuiSliderFlags() {
        throw new AssertionError();
    }
}
