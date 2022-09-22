package imgui.api;

public final class ImGuiKeyData {
    public boolean down;
    public float downDuration;
    public float downDurationPrev;
    public float analogValue;

    public void set(ImGuiKeyData src) {
        down = src.down;
        downDuration = src.downDuration;
        downDurationPrev = src.downDurationPrev;
        analogValue = src.analogValue;
    }
}
