package imgui.flag;

public final class ImGuiConfigFlags {
    public static final int None = 0;
    public static final int NavEnableKeyboard = 1;
    public static final int NavEnableGamepad = 1 << 1;
    public static final int NavEnableSetMousePos = 1 << 2;
    public static final int NavNoCaptureKeyboard = 1 << 3;
    public static final int NoMouse = 1 << 4;
    public static final int NoMouseCursorChange = 1 << 5;

    public static final int DockingEnable = 1 << 6;

    public static final int ViewportsEnable = 1 << 10;
    public static final int DpiEnableScaleViewports = 1 << 14;
    public static final int DpiEnableScaleFonts = 1 << 15;

    public static final int IsSRGB = 1 << 20;
    public static final int IsTouchScreen = 1 << 21;

    private ImGuiConfigFlags() {
        throw new AssertionError();
    }
}
