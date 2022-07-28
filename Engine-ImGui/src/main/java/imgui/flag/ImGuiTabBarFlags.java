package imgui.flag;

public final class ImGuiTabBarFlags {
    public static final int None = 0;
    public static final int Reorderable = 1;
    public static final int AutoSelectNewTabs = 1 << 1;
    public static final int TabListPopupButton = 1 << 2;
    public static final int NoCloseWithMiddleMouseButton = 1 << 3;
    public static final int NoTabListScrollingButtons = 1 << 4;
    public static final int NoTooltip = 1 << 5;
    public static final int FittingPolicyResizeDown = 1 << 6;
    public static final int FittingPolicyScroll = 1 << 7;

    public static final int FittingPolicyMask_ = FittingPolicyResizeDown | FittingPolicyScroll;
    public static final int FittingPolicyDefault = FittingPolicyResizeDown;

    private ImGuiTabBarFlags() {
        throw new AssertionError();
    }
}
