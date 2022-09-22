package imgui.api;

import imgui.util.ManagedBuffer;

public final class ImFontConfig {
    public ManagedBuffer fontData;
    public boolean fontDataOwnedByAtlas;
    public int fontNo;
    public float sizePixels;
    public int oversampleH;
    public int oversampleV;
    public boolean pixelSnapH;
    public final ImVec2 glyphExtraSpacing = new ImVec2();
    public final ImVec2 glyphOffset = new ImVec2();
    public char[] glyphRanges;
    public float glyphMinAdvanceX;
    public float glyphMaxAdvanceX;
    public boolean mergeMode;
    public int fontBuilderFlags;
    public float rasterizerMultiply;
    public char ellipsisChar;

    public String name;
    public ImFont/*ptr*/ dstFont;

    public ImFontConfig() {

    }
}
