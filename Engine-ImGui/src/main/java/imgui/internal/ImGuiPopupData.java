package imgui.internal;

import imgui.api.ImVec2;

public final class ImGuiPopupData {
    public int popupId;
    public ImGuiWindow/*ptr*/ window;
    public ImGuiWindow/*ptr*/ sourceWindow;
    public int parentNavLayer;
    public int openFrameCount;
    public int openParentId;
    public final ImVec2 openPopupPos = new ImVec2();
    public final ImVec2 openMousePos = new ImVec2();

    public ImGuiPopupData() {
        parentNavLayer = openFrameCount = -1;
    }
}
