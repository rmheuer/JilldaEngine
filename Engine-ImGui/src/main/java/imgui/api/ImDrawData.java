package imgui.api;

public final class ImDrawData {
    public boolean valid;
    public int totalIdxCount;
    public int totalVtxCount;
    public ImDrawList[] cmdLists;
    public final ImVec2 displayPos = new ImVec2();
    public final ImVec2 displaySize = new ImVec2();
    public final ImVec2 framebufferScale = new ImVec2();
    public ImGuiViewport/*ptr*/ ownerViewport;

    public ImDrawData() {
        clear();
    }

    public void clear() {
        valid = false;
        totalIdxCount = 0;
        totalVtxCount = 0;
        cmdLists = null;
        displayPos.set(0, 0);
        displaySize.set(0, 0);
        framebufferScale.set(0, 0);
        ownerViewport = null;
    }

    public void deIndexAllBuffers() {

    }

    public void scaleClipRects(ImVec2/*ref*/ fbScale) {

    }
}
