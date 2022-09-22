package imgui.api;

public final class ImGuiViewport {
    public int id;
    public int flags;
    public final ImVec2 pos = new ImVec2();
    public final ImVec2 size = new ImVec2();
    public final ImVec2 workPos = new ImVec2();
    public final ImVec2 workSize = new ImVec2();
    public float dpiScale;
    public int parentViewportId;
    public ImDrawData/*ptr*/ drawData;

    public Object rendererUserData;
    public Object platformUserData;
    public Object platformHandle;
    public Object platformHandleRaw;
    public boolean platformRequestMove;
    public boolean platformRequestResize;
    public boolean platformRequestClose;

    public ImVec2 getCenter() {
        return new ImVec2(pos.x + size.x * 0.5f, pos.y + size.y * 0.5f);
    }

    public ImVec2 getWorkCenter() {
        return new ImVec2(workPos.x + workSize.x * 0.5f, workPos.y + workSize.y * 0.5f);
    }
}
