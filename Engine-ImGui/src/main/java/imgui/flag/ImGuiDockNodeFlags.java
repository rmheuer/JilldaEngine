package imgui.flag;

public final class ImGuiDockNodeFlags {
    public static final int None = 0;
    public static final int KeepAliveOnly = 1;
    public static final int NoDockingInCentralNode = 1 << 2;
    public static final int PassthruCentralNode = 1 << 3;
    public static final int NoSplit = 1 << 4;
    public static final int NoResize = 1 << 5;
    public static final int AutoHideTabBar = 1 << 6;

    private ImGuiDockNodeFlags() {
        throw new AssertionError();
    }
}
