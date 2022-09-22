package imgui.api;

public final class ImFontAtlasCustomRect {
    public short width, height;
    public short x, y;
    public int glyphID;
    public float glyphAdvanceX;
    public final ImVec2 glyphOffset = new ImVec2();
    public ImFont/*ptr*/ font;

    public ImFontAtlasCustomRect() {
        width = height = 0;
        x = y = (short) 0xFFFF;
        glyphID = 0;
        glyphAdvanceX = 0.0f;
        glyphOffset.set(0, 0);
        font = null;
    }

    public boolean isPacked() {
        return x != (short) 0xFFFF;
    }
}
