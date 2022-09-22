package imgui.api;

import com.github.rmheuer.engine.core.util.SizeOf;

import java.util.ArrayList;
import java.util.List;

import static imgui.api.ImGuiMacros.IM_UNICODE_CODEPOINT_MAX;

public final class ImFontGlyphRangesBuilder {
    public final List<Integer> usedChars = new ArrayList<>();

    public ImFontGlyphRangesBuilder() {
        clear();
    }

    public void clear() {
        int sizeInBytes = (IM_UNICODE_CODEPOINT_MAX + 1) / 8;
        usedChars.clear();
        for (int i = 0; i < sizeInBytes / SizeOf.INT; i++) {
            usedChars.add(0);
        }
    }

    public boolean getBit(int n) {
        int off = n >> 5;
        int mask = 1 << (n & 31);
        return (usedChars.get(off) & mask) != 0;
    }

    public void setBit(int n) {
        int off = n >> 5;
        int mask = 1 << (n & 31);
        usedChars.set(off, usedChars.get(off) | mask);
    }

    public void addChar(char c) {
        setBit(c);
    }

    public void addText(String text) {

    }

    public void addRanges(char[] ranges) {

    }

    public void buildRanges(List<Character>/*ptr*/ outRanges) {

    }
}
