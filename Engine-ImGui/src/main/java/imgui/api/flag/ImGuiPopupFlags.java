package imgui.api.flag;

public final class ImGuiPopupFlags {
    public static final int None = 0;
    public static final int MouseButtonLeft = 0;
    public static final int MouseButtonRight = 1;
    public static final int MouseButtonMiddle = 2;
    public static final int MouseButtonMask_ = 0x1F;
    public static final int MouseButtonDefault_ = 1;
    public static final int NoOpenOverExistingPopup = 1 << 5;
    public static final int NoOpenOverItems = 1 << 6;
    public static final int AnyPopupId = 1 << 7;
    public static final int AnyPopupLevel = 1 << 8;

    public static final int AnyPopup = AnyPopupId | AnyPopupLevel;

    private ImGuiPopupFlags() {
        throw new AssertionError();
    }
}
