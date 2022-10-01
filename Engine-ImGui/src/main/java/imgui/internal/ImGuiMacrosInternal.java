package imgui.internal;

import static imgui.api.ImGuiMacros.IM_ASSERT;
import static imgui.internal.ImGuiInternal.imClamp;
import static imgui.internal.ImGuiInternal.imMax;
import static imgui.internal.ImGuiInternal.imMin;
import static imgui.internal.ImGuiInternal.imSaturate;

public final class ImGuiMacrosInternal {
    public static final String IMGUI_PAYLOAD_TYPE_WINDOW = "_IMWINDOW";

    public static void IMGUI_DEBUG_PRINTF(String fmt, Object... fmtArgs) {
        System.out.printf(fmt, fmtArgs);
    }

    public static void IM_STATIC_ASSERT(boolean b) {
        // Java does not have static assertions, do it at runtime
        IM_ASSERT(b);
    }

    public static void IM_ASSERT_PARANOID(boolean b) {
        IM_ASSERT(b);
    }

    public static void IM_ASSERT_USER_ERROR(boolean b, String msg) {
        IM_ASSERT(b, msg);
    }

    public static final float IM_PI = 3.14159265358979323846f;
    public static final String IM_NEWLINE = "\n";
    public static final int IM_TABSIZE = 4;
    public static int IM_F32_TO_INT8_UNBOUND(float val) {
        return (int) (val * 255.0f + (val >= 0 ? 0.5f : -0.5f));
    }
    public static int IM_F32_TO_INT8_SAT(float val) {
        return (int) (imSaturate(val) * 255.0f + 0.5f);
    }
    public static float IM_FLOOR(float val) {
        return (int) val;
    }
    public static float IM_ROUND(float val) {
        return (int) (val + 0.5f);
    }

    public static void IM_DEBUG_BREAK() {
        // Place a breakpoint here
        IM_ASSERT(false);
    }

    public static float imFabs(float x) {
        return Math.abs(x);
    }

    public static float imSqrt(float x) {
        return (float) Math.sqrt(x);
    }

    private static float trunc(float x) {
        return x < 0 ? (float) -Math.floor(-x) : (float) Math.floor(x);
    }

    public static float imFmod(float x, float y) {
        return x - trunc(x / y) * y;
    }

    public static float imCos(float x) {
        return (float) Math.cos(x);
    }

    public static float imSin(float x) {
        return (float) Math.sin(x);
    }

    public static float imAcos(float x) {
        return (float) Math.acos(x);
    }

    public static float imAtan2(float y, float x) {
        return (float) Math.atan2(y, x);
    }

    public static float imAtof(String str) {
        return Float.parseFloat(str);
    }

    public static float imCeil(float x) {
        return (float) Math.ceil(x);
    }

    public static int IM_ROUNDUP_TO_EVEN(int v) {
        return ((v + 1) / 2) * 2;
    }

    public static final int IM_DRAWLIST_CIRCLE_AUTO_SEGMENT_MIN = 4;
    public static final int IM_DRAWLIST_CIRCLE_AUTO_SEGMENT_MAX = 512;
    public static int IM_DRAWLIST_CIRCLE_AUTO_SEGMENT_CALC(float rad, float maxError) {
        return imClamp(IM_ROUNDUP_TO_EVEN((int) imCeil(IM_PI / imAcos(1 - imMin(maxError, rad) / rad))), IM_DRAWLIST_CIRCLE_AUTO_SEGMENT_MIN, IM_DRAWLIST_CIRCLE_AUTO_SEGMENT_MAX);
    }

    public static float IM_DRAWLIST_CIRCLE_AUTO_SEGMENT_CALC_R(int n, float maxError) {
        return maxError / (1 - imCos(IM_PI / imMax(n, IM_PI)));
    }

    public static float IM_DRAWLIST_CIRCLE_AUTO_SEGMENT_CALC_ERROR(int n, float rad) {
        return ((1 - imCos(IM_PI / imMax(n, IM_PI))) / rad);
    }

    public static final int IM_DRAWLIST_ARCFAST_TABLE_SIZE = 48;
    public static final int IM_DRAWLIST_ARCFAST_SAMPLE_MAX = IM_DRAWLIST_ARCFAST_TABLE_SIZE;

    private ImGuiMacrosInternal() {
        throw new AssertionError();
    }
}
