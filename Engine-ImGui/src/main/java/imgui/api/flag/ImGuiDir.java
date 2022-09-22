package imgui.api.flag;

public final class ImGuiDir {
    public static final int None = -1;
    public static final int Left = 0;
    public static final int Right = 1;
    public static final int Up = 2;
    public static final int Down = 3;
    public static final int COUNT = 4;

    private ImGuiDir() {
        throw new AssertionError();
    }
}
