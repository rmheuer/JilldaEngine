package imgui.internal;

import imgui.api.ImVec2;

public final class ImGuiStyleMod {
    public int varIdx;
    public Object backup;

    public ImGuiStyleMod(int idx, int v) {
        varIdx = idx;
        backup = v;
    }

    public ImGuiStyleMod(int idx, float v) {
        varIdx = idx;
        backup = v;
    }

    public ImGuiStyleMod(int idx, ImVec2 v) {
        varIdx = idx;
        backup = new ImVec2(v);
    }
}
