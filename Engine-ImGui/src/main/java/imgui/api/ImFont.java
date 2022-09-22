package imgui.api;

import java.util.ArrayList;
import java.util.List;

import static imgui.api.ImGuiMacros.IM_UNICODE_CODEPOINT_MAX;

public final class ImFont {
    public final List<Float> indexAdvanceX = new ArrayList<>();
    public float fallbackAdvanceX;
    public float fontSize;

    public final List<Character> indexLookup = new ArrayList<>();
    public final List<ImFontGlyph> glyphs = new ArrayList<>();
    public ImFontGlyph/*ptr*/ fallbackGlyph;

    public ImFontAtlas/*ptr*/ containerAtlas;
    public ImFontConfig/*ptr*/ configData;
    public short configDataCount;
    public char fallbackChar;
    public char ellipsisChar;
    public char dotChar;
    public boolean dirtyLookupTables;
    public float scale;
    public float ascent, descent;
    public int metricsTotalSurface;
    public final byte[] used4kPagesMap = new byte[(IM_UNICODE_CODEPOINT_MAX + 1) / 4096 / 8];

    public ImFont() {

    }

    public ImFontGlyph/*ptr*/ fontGlyph(char c) {
        return null;
    }

    public ImFontGlyph/*ptr*/ findGlyphNoFallback(char c) {
        return null;
    }

    public float getCharAdvance(char c) {
        return (c < indexAdvanceX.size()) ? indexAdvanceX.get(c) : fallbackAdvanceX;
    }

    public boolean isLoaded() {
        return containerAtlas != null;
    }

    public String getDebugName() {
        return configData != null ? configData.name : "<unknown>";
    }

    public ImVec2 calcTextSizeA(float size, float maxWidth, float wrapWidth, String text) {
        return null;
    }

    public int calcWordWrapPositionA(float scale, String text, float wrapWidth) {
        return 0;
    }

    public void renderChar(ImDrawList/*ptr*/ drawList, float size, ImVec2 pos, int col, char c) {

    }

    public void renderText(ImDrawList drawList, float size, ImVec2 pos, int col, ImVec4 clipRect, String text) { renderText(drawList, size, pos, col, clipRect, text, 0.0f); }
    public void renderText(ImDrawList drawList, float size, ImVec2 pos, int col, ImVec4 clipRect, String text, float wrapWidth) { renderText(drawList, size, pos, col, clipRect, text, wrapWidth, false); }
    public void renderText(ImDrawList/*ptr*/ drawList, float size, ImVec2 pos, int col, ImVec4 clipRect, String text, float wrapWidth, boolean cpuFineClip) {

    }

    public void buildLookupTable() {

    }

    public void clearOutputData() {

    }

    public void growIndex(int newSize) {

    }

    public void addGlyph(ImFontConfig/*ptr*/ srcConfig, char c, float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1, float advanceX) {

    }

    public void addRemapChar(char dst, char src) { addRemapChar(dst, src, true); }
    public void addRemapChar(char dst, char src, boolean overwriteDst) {

    }

    public void setGlyphVisible(char c, boolean visible) {

    }

    public boolean isGlyphRangeUnused(int cBegin, int cLast) {
        return false;
    }
}
