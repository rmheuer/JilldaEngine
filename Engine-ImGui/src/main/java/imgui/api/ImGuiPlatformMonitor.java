package imgui.api;

public final class ImGuiPlatformMonitor {
    public final ImVec2 mainPos = new ImVec2(), mainSize = new ImVec2();
    public final ImVec2 workPos = new ImVec2(), workSize = new ImVec2();
    public float dpiScale;

    public ImGuiPlatformMonitor() {
        dpiScale = 1.0f;
    }
}
