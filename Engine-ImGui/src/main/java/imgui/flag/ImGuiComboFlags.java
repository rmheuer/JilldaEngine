package imgui.flag;

public final class ImGuiComboFlags {
    public static final int None = 0;
    public static final int PopupAlignLeft = 1;
    public static final int HeightSmall = 1 << 1;
    public static final int HeightRegular = 1 << 2;
    public static final int HeightLarge = 1 << 3;
    public static final int HeightLargest = 1 << 4;
    public static final int NoArrowButton = 1 << 5;
    public static final int NoPreview = 1 << 6;

    public static final int HeightMask_ = HeightSmall | HeightRegular | HeightLarge | HeightLargest;

    private ImGuiComboFlags() {
        throw new AssertionError();
    }
}
