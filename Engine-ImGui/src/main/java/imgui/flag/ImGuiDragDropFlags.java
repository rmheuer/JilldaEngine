package imgui.flag;

public final class ImGuiDragDropFlags {
    public static final int None = 0;

    public static final int SourceNoPreviewTooltip = 1;
    public static final int SourceNoDisableHover = 1 << 1;
    public static final int SourceNoHoldToOpenOthers = 1 << 2;
    public static final int SourceAllowNullID = 1 << 3;
    public static final int SourceExtern = 1 << 4;
    public static final int SourceAutoExpirePayload = 1 << 5;

    public static final int AcceptBeforeDelivery = 1 << 10;
    public static final int AcceptNoDrawDefaultRect = 1 << 11;
    public static final int AcceptNoPreviewTooltip = 1 << 12;
    public static final int AcceptPeekOnly = AcceptBeforeDelivery | AcceptNoDrawDefaultRect;

    private ImGuiDragDropFlags() {
        throw new AssertionError();
    }
}
