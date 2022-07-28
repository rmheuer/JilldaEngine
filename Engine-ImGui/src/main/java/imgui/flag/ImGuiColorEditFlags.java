package imgui.flag;

public final class ImGuiColorEditFlags {
    public static final int None = 0;
    public static final int NoAlpha = 1;
    public static final int NoPicker = 1 << 2;
    public static final int NoOptions = 1 << 3;
    public static final int NoSmallPreview = 1 << 4;
    public static final int NoInputs = 1 << 5;
    public static final int NoTooltip = 1 << 6;
    public static final int NoLabel = 1 << 7;
    public static final int NoSidePreview = 1 << 8;
    public static final int NoDragDrop = 1 << 9;
    public static final int NoBorder = 1 << 10;

    public static final int AlphaBar = 1 << 16;
    public static final int AlphaPreview = 1 << 17;
    public static final int AlphaPreviewHalf = 1 << 18;
    public static final int HDR = 1 << 19;
    public static final int DisplayRGB = 1 << 20;
    public static final int DisplayHSV = 1 << 21;
    public static final int DisplayHex = 1 << 22;
    public static final int Uint8 = 1 << 23;
    public static final int Float = 1 << 24;
    public static final int PickerHueBar = 1 << 25;
    public static final int PickerHueWheel = 1 << 26;
    public static final int InputRGB = 1 << 27;
    public static final int InputHSV = 1 << 28;

    public static final int DefaultOptions_ = Uint8 | DisplayRGB | InputRGB | PickerHueBar;

    public static final int DisplayMask_ = DisplayRGB | DisplayHSV | DisplayHex;
    public static final int DataTypeMask_ = Uint8 | Float;
    public static final int PickerMask_ = PickerHueWheel | PickerHueBar;
    public static final int InputMask_ = InputRGB | InputHSV;

    private ImGuiColorEditFlags() {
        throw new AssertionError();
    }
}
