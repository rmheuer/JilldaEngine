package imgui.api;

import imgui.util.ManagedBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static imgui.api.ImGuiMacros.IM_ASSERT;
import static imgui.api.ImGuiMacros.IM_DRAWLIST_TEX_LINES_WIDTH_MAX;

public final class ImFontAtlas {
    public ImFontAtlas() {
        for (int i = 0; i < texUvLines.length; i++)
            texUvLines[i] = new ImVec4();
    }

    public ImFont/*ptr*/ addFont(ImFontConfig/*ptr*/ fontCfg) {
        return null;
    }

    public ImFont addFontDefault() { return addFontDefault(null); }
    public ImFont/*ptr*/ addFontDefault(ImFontConfig/*ptr*/ fontCfg) {
        return null;
    }

    public ImFont addFontFromFileTTF(String filename, float sizePixels) { return addFontFromFileTTF(filename, sizePixels, null); }
    public ImFont addFontFromFileTTF(String filename, float sizePixels, ImFontConfig fontCfg) { return addFontFromFileTTF(filename, sizePixels, fontCfg, null); }
    public ImFont/*ptr*/ addFontFromFileTTF(String filename, float sizePixels, ImFontConfig fontCfg, char[] glyphRanges) {
        return null;
    }

    public ImFont addFontFromMemoryTTF(ManagedBuffer fontData, float sizePixels) { return addFontFromMemoryTTF(fontData, sizePixels, null); }
    public ImFont addFontFromMemoryTTF(ManagedBuffer fontData, float sizePixels, ImFontConfig fontCfg) { return addFontFromMemoryTTF(fontData, sizePixels, fontCfg, null); }
    public ImFont addFontFromMemoryTTF(ManagedBuffer fontData, float sizePixels, ImFontConfig fontCfg, char[] glyphRanges) {
        return null;
    }

    // TODO: Compressed

    public void clearInputData() {

    }

    public void clearTexData() {

    }

    public void clearFonts() {

    }

    public void clear() {

    }

    public boolean build() {
        return false;
    }

    public void getTexDataAsAlpha8(ByteBuffer outPixels, int[] outWidth, int[] outHeight) { getTexDataAsAlpha8(outPixels, outWidth, outHeight, null); }
    public void getTexDataAsAlpha8(ByteBuffer outPixels, int[] outWidth, int[] outHeight, int[] outBytesPerPixel) {

    }

    public void getTexDataAsRGBA32(ByteBuffer outPixels, int[] outWidth, int[] outHeight) { }
    public void getTexDataAsRGBA32(ByteBuffer outPixels, int[] outWidth, int[] outHeight, int[] outBytesPerPixel) {

    }

    public boolean isBuilt() {
        return fonts.size() > 0 && texReady;
    }

    public void setTexId(int id) {
        texID = id;
    }

    public char[] getGlyphRangesDefault() {
        return null;
    }

    public char[] getGlyphRangesKorean() {
        return null;
    }

    public char[] getGlyphRangesJapanese() {
        return null;
    }

    public char[] getGlyphRangesChineseFull() {
        return null;
    }

    public char[] getGlyphRangesChineseSimplifiedCommon() {
        return null;
    }

    public char[] getGlyphRangesCyrillic() {
        return null;
    }

    public char[] getGlyphRangesThai() {
        return null;
    }

    public char[] getGlyphRangesVietnamese() {
        return null;
    }

    public int addCustomRectRegular(int width, int height) {
        return 0;
    }

    public int addCustomRectFontGlyph(ImFont font, char id, int width, int height, float advanceX) { return addCustomRectFontGlyph(font, id, width, height, advanceX, new ImVec2(0, 0)); }
    public int addCustomRectFontGlyph(ImFont/*ptr*/ font, char id, int width, int height, float advanceX, ImVec2/*ref*/ offset) {
        return 0;
    }

    public ImFontAtlasCustomRect/*ptr*/ getCustomRectByIndex(int index) {
        IM_ASSERT(index >= 0);
        return customRects.get(index);
    }

    public int flags;
    public int texID;
    public int texDesiredWidth;
    public int texGlyphPadding;
    public boolean locked;

    public boolean texReady;
    public boolean texPixelsUseColors;
    public ByteBuffer texPixelsAlpha8;
    public ByteBuffer texPixelsRGBA32;
    public int texWidth;
    public int texHeight;
    public final ImVec2 texUvScale = new ImVec2();
    public final ImVec2 texUvWhitePixel = new ImVec2();
    public final List<ImFont/*ptr*/> fonts = new ArrayList<>();
    public final List<ImFontAtlasCustomRect> customRects = new ArrayList<>();
    public final List<ImFontConfig> configData = new ArrayList<>();
    private final ImVec4[] texUvLines = new ImVec4[IM_DRAWLIST_TEX_LINES_WIDTH_MAX + 1];

    public ImVec4 getTexUvLine(int idx) {
        return texUvLines[idx];
    }

    public void setTexUvLine(int idx, ImVec4 val) {
        texUvLines[idx].set(val);
    }

    public ImFontBuilderIO/*ptr*/ fontBuilderIO;
    public int fontBuilderFlags;

    public int packIdMouseCursors;
    public int packIdLines;
}
