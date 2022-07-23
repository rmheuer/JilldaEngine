package imgui;

public final class ImGuiMacros {
    public static final float FLT_MIN = Float.MIN_VALUE;

    public static void IM_ASSERT(boolean b) {
        if (!b) {
            throw new AssertionError();
        }
    }

    private ImGuiMacros() {
        throw new AssertionError();
    }
}
