package imgui.internal.flag;

public final class ImGuiSelectableFlagsPrivate {
    public static final int NoHoldingActiveID = 1 << 20;
    public static final int SelectOnNav = 1 << 21;
    public static final int SelectOnClick = 1 << 22;
    public static final int SelectOnRelease = 1 << 23;
    public static final int SpanAvailWidth = 1 << 24;
    public static final int DrawHoveredWhenHeld = 1 << 25;
    public static final int SetNavIdOnHover = 1 << 26;
    public static final int NoPadWithHalfSpacing = 1 << 27;

    private ImGuiSelectableFlagsPrivate() {
        throw new AssertionError();
    }
}
