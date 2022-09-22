package imgui.api.flag;

public final class ImGuiTableBgTarget {
    public static final int None = 0;
    public static final int RowBg0 = 1;
    public static final int RowBg1 = 2;
    public static final int CellBg = 3;

    private ImGuiTableBgTarget() {
        throw new AssertionError();
    }
}
