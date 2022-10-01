package imgui.internal.flag;

public final class ImGuiSliderFlagsPrivate {
    public static final int Vertical = 1 << 20;
    public static final int ReadOnly = 1 << 21;

    private ImGuiSliderFlagsPrivate() {
        throw new AssertionError();
    }
}
