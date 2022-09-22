package imgui.api.flag;

public final class ImDrawListFlags {
    public static final int None = 0;
    public static final int AntiAliasedLines = 1;
    public static final int AntiAliasedLinesUseTex = 1 << 1;
    public static final int AntiAliasedFill = 1 << 2;
    public static final int AllowVtxOffset = 1 << 3;

    private ImDrawListFlags() {
        throw new AssertionError();
    }
}
