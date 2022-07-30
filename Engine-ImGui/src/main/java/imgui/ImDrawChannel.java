package imgui;

public final class ImDrawChannel {
    public ImVector<ImDrawCmd> _CmdBuffer = new ImVector<>();
    public ImVector<Integer> _IdxBuffer = new ImVector<>();
}
