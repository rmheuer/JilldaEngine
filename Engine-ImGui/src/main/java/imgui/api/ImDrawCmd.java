package imgui.api;

import imgui.api.fn.ImDrawCallback;

public final class ImDrawCmd {
    public final ImVec4 clipRect = new ImVec4();
    public int textureId;
    public int vtxOffset;
    public int idxOffset;
    public int elemCount;
    public ImDrawCallback/*implicit ptr*/ userCallback;
    public Object userCallbackData;

    public int getTexId() {
        return textureId;
    }
}
