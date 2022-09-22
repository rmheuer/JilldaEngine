package imgui.api;

public final class ImGuiListClipper {
    public int displayStart;
    public int displayEnd;
    public int itemsCount;
    public float itemsHeight;
    public float startPosY;
    public Object tempData;

    public ImGuiListClipper() {

    }

    public void begin(int itemsCount) { begin(itemsCount, -1.0f); }
    public void begin(int itemsCount, float itemsHeight) {

    }

    public void end() {

    }

    public boolean step() {
        return false;
    }

    public void forceDisplayRangeByIndices(int itemMin, int itemMax) {

    }
}
