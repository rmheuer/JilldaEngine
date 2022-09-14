package imgui.flag;

public final class ImFontAtlasFlags {
    public static final int None = 0;
    public static final int NoPowerOfTwoHeight = 1;
    public static final int NoMouseCursors = 1 << 1;
    public static final int NoBakedLines = 1 << 2;

    private ImFontAtlasFlags() {
        throw new AssertionError();
    }
}
