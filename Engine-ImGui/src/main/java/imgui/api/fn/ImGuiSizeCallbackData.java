package imgui.api.fn;

import imgui.api.ImVec2;

public final class ImGuiSizeCallbackData {
    public Object userData;
    public ImVec2 pos = new ImVec2();
    public ImVec2 currentSize = new ImVec2();
    public ImVec2 desiredSize = new ImVec2();
}
