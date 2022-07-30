package imgui;

public final class ImGuiWindowClass {
    public int classId;
    public int parentViewportId;
    public int viewportFlagsOverrideSet;
    public int viewportFlagsOverrideClear;
    public int tabItemFlagsOverrideSet;
    public int dockNodeFlagsOverrideSet;
    public boolean dockingAlwaysTabBar;
    public boolean dockingAllowUnclassed;

    public ImGuiWindowClass() {
        parentViewportId = -1;
        dockingAllowUnclassed = true;
    }
}
