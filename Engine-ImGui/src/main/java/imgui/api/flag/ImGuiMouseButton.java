package imgui.api.flag;

public final class ImGuiMouseButton {
    public static final int Left = 0;
    public static final int Right = 1;
    public static final int Middle = 2;
    public static final int COUNT = 5;

    private ImGuiMouseButton() {
        throw new AssertionError();
    }
}
