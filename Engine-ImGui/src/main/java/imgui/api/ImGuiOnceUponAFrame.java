package imgui.api;

public final class ImGuiOnceUponAFrame {
    public int refFrame;

    public ImGuiOnceUponAFrame() {
        refFrame = -1;
    }

    public boolean get() {
        int currentFrame = ImGui.getFrameCount();
        if (refFrame == currentFrame)
            return false;
        refFrame = currentFrame;
        return true;
    }
}
