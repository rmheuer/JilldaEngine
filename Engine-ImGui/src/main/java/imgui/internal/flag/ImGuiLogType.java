package imgui.internal.flag;

public final class ImGuiLogType {
    public static final int None = 0;
    public static final int TTY = 1;
    public static final int File = 2;
    public static final int Buffer = 3;
    public static final int Clipboard = 4;

    public ImGuiLogType() {
        throw new AssertionError();
    }
}
