package imgui.api.flag;

public final class ImGuiSortDirection {
    public static final int None = 0;
    public static final int Ascending = 1;
    public static final int Descending = 2;

    private ImGuiSortDirection() {
        throw new AssertionError();
    }
}
