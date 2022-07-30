package imgui;

import imgui.fn.ImDrawCallback;

public final class ImGuiMacros {
    public static final float FLT_MIN = Float.MIN_VALUE;

    public static final String IMGUI_PAYLOAD_TYPE_COLOR_3F = "_COL3F";
    public static final String IMGUI_PAYLOAD_TYPE_COLOR_4F = "_COL4F";

    public static final char IM_UNICODE_CODEPOINT_INVALID = 0xFFFD;
    public static final char IM_UNICODE_CODEPOINT_MAX = 0xFFFF;

    public static final int IM_COL32_R_SHIFT = 0;
    public static final int IM_COL32_G_SHIFT = 8;
    public static final int IM_COL32_B_SHIFT = 16;
    public static final int IM_COL32_A_SHIFT = 24;
    public static final int IM_COL32_A_MASK = 0xFF000000;
    public static final int IM_COL32_WHITE = IM_COL32(255, 255, 255, 255);
    public static final int IM_COL32_BLACK = IM_COL32(0, 0, 0, 255);
    public static final int IM_COL32_BLACK_TRANS = IM_COL32(0, 0, 0, 0);

    public static final int IM_DRAWLIST_TEX_LINES_WIDTH_MAX = 63;

    // Check identity equality (==)
    public static final ImDrawCallback ImDrawCallback_ResetRenderState = (p, c) -> {};

    public static void IM_ASSERT(boolean b) {
        if (!b) {
            throw new AssertionError();
        }
    }

    public static int IM_COL32(int r, int g, int b, int a) {
        return (a << IM_COL32_A_SHIFT) | (b << IM_COL32_B_SHIFT) | (g << IM_COL32_G_SHIFT) | (r << IM_COL32_R_SHIFT);
    }

    private ImGuiMacros() {
        throw new AssertionError();
    }
}
