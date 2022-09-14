package imgui;

public final class ImGuiStyle {
    public float alpha;
    public float disabledAlpha;
    private ImVec2 windowPadding;

    public ImVec2 getWindowPadding() {
        return new ImVec2(windowPadding);
    }

    public void setWindowPadding(ImVec2 windowPadding) {
        this.windowPadding = new ImVec2(windowPadding);
    }
}
