package imgui.internal.flag;

public final class ImGuiInputTextFlagsPrivate {
    public static final int Multiline = 1 << 26;
    public static final int NoMarkEdited = 1 << 27;
    public static final int MergedItem = 1 << 28;

    private ImGuiInputTextFlagsPrivate() {
        throw new AssertionError();
    }
}
