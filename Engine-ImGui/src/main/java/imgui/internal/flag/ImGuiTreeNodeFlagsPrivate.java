package imgui.internal.flag;

public final class ImGuiTreeNodeFlagsPrivate {
    public static final int ClipLabelForTrailingButton = 1 << 20;

    private ImGuiTreeNodeFlagsPrivate() {
        throw new AssertionError();
    }
}
