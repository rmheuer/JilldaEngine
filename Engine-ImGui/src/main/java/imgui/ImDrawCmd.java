package imgui;

import imgui.fn.ImDrawCallback;

public final class ImDrawCmd {
    public ImVec4 clipRect = new ImVec4();
    public Object textureId;
    public int vtxOffset;
    public int idxOffset;
    public int elemCount;
    public ImDrawCallback userCallback;
    public Object userCallbackData;

    public Object getTexID() {
        return textureId;
    }
}
