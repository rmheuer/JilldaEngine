package imgui.api.flag;

public final class ImGuiButtonFlags {
    public static final int None = 0;
    public static final int MouseButtonLeft = 1;
    public static final int MouseButtonRight = 1 << 1;
    public static final int MouseButtonMiddle = 1 << 2;

    public static final int MouseButtonMask_ = MouseButtonLeft | MouseButtonRight | MouseButtonMiddle;
    public static final int MouseButtonDefault_ = MouseButtonLeft;

    private ImGuiButtonFlags() {
        throw new AssertionError();
    }
}
