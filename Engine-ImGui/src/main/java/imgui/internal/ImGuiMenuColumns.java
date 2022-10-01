package imgui.internal;

public final class ImGuiMenuColumns {
    public int totalWidth;
    public int nextTotalWidth;
    public short spacing;
    public short offsetIcon;
    public short offsetLabel;
    public short offsetShortcut;
    public short offsetMark;
    public final short[] widths = new short[4];

    public void update(float spacing, boolean windowReappearing) {

    }

    public float declColumns(float wIcon, float wLabel, float wShortcut, float wMark) {
        return 0;
    }

    public void calcNextTotalWidth(boolean updateOffsets) {

    }
}
