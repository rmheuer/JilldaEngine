package imgui.api.flag;

public final class ImGuiInputTextFlags {
    public static final int None = 0;
    public static final int CharsDecimal = 1;
    public static final int CharsHexadecimal = 1 << 1;
    public static final int CharsUppercase = 1 << 2;
    public static final int CharsNoBlank = 1 << 3;
    public static final int AutoSelectAll = 1 << 4;
    public static final int EnterReturnsTrue = 1 << 5;
    public static final int CallbackCompletion = 1 << 6;
    public static final int CallbackHistory = 1 << 7;
    public static final int CallbackAlways = 1 << 8;
    public static final int CallbackCharFilter = 1 << 9;
    public static final int AllowTabInput = 1 << 10;
    public static final int CtrlEnterForNewLine = 1 << 11;
    public static final int NoHorizontalScroll = 1 << 12;
    public static final int AlwaysOverwrite = 1 << 13;
    public static final int ReadOnly = 1 << 14;
    public static final int Password = 1 << 15;
    public static final int NoUndoRedo = 1 << 16;
    public static final int CharsScientific = 1 << 17;
    public static final int CallbackResize = 1 << 18;
    public static final int CallbackEdit = 1 << 19;

    private ImGuiInputTextFlags() {
        throw new AssertionError();
    }
}
