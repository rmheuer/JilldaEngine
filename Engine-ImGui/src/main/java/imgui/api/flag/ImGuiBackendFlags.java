package imgui.api.flag;

public final class ImGuiBackendFlags {
    public static final int None = 0;
    public static final int HasGamepad = 1;
    public static final int HasMouseCursors = 1 << 1;
    public static final int HasSetMousePos = 1 << 2;
    public static final int RendererHasVtxOffset = 1 << 3;

    public static final int PlatformHasViewports = 1 << 10;
    public static final int HasMouseHoveredViewport = 1 << 11;
    public static final int RendererHasViewports = 1 << 12;

    private ImGuiBackendFlags() {
        throw new AssertionError();
    }
}
