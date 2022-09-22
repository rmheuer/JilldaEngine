package imgui.api;

public final class ImGuiSizeCallbackData {
    public Object userData;
    public final ImVec2 pos = new ImVec2();
    public final ImVec2 currentSize = new ImVec2();
    public final ImVec2 desiredSize = new ImVec2();
}
