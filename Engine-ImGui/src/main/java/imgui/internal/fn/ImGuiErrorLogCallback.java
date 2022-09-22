package imgui.internal.fn;

@FunctionalInterface
public interface ImGuiErrorLogCallback {
    void call(Object userData, String fmt, Object... fmtArgs);
}
