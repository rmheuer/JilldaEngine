package imgui.api;

import static imgui.internal.ImGuiMacrosInternal.IM_DRAWLIST_ARCFAST_TABLE_SIZE;

public final class ImDrawListSharedData {
    public final ImVec2 texUvWhitePixel = new ImVec2();
    public ImFont/*ptr*/ font;
    public float fontSize;
    public float curveTessellationTol;
    public float circleSegmentMaxError;
    public final ImVec4 clipRectFullscreen = new ImVec4();
    public int initialFlags;

    private final ImVec2[] arcFastVtx = new ImVec2[IM_DRAWLIST_ARCFAST_TABLE_SIZE];
    public float arcFastRadiusCutoff;
    public byte[] circleSegmentCounts = new byte[64];
    public ImVec4 texUvLines;

    public ImDrawListSharedData() {

    }

    public void setCircleTessellationMaxError(float maxError) {

    }

    public ImVec2 getArcFastVtx(int idx) {
        return arcFastVtx[idx];
    }

    public void setArcFastVtx(int idx, ImVec2 vec) {
        arcFastVtx[idx].set(vec);
    }
}
