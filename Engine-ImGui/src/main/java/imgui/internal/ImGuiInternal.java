package imgui.internal;

import imgui.api.ImGuiContext;
import imgui.api.ImVec2;

public final class ImGuiInternal {
    public static ImGuiContext/*ptr*/ gImGui;

    public static int imHashData(byte[] data) { return imHashData(data, 0); }
    public static int imHashData(byte[] data, int seed) {
        return 0;
    }

    public static int imHashStr(String str) { return imHashStr(str, 0); }
    public static int imHashStr(String str, int seed) {
        return 0;
    }

    public static int imAlphaBlendColors(int colA, int colB) {
        return 0;
    }

    public static boolean imIsPowerOfTwo(long v) {
        return v != 0 && (v & (v - 1)) == 0;
    }

    public static int imUpperPowerOfTwo(int v) {
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v++;
        return v;
    }

    public static float imPow(float x, float y) {
        return (float) Math.pow(x, y);
    }

    public static double imPow(double x, double y) {
        return Math.pow(x, y);
    }

    public static float imLog(float x) {
        return (float) Math.log(x);
    }

    public static double imLog(double x) {
        return Math.log(x);
    }

    public static ImVec2 imBezierCubicCalc(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, float t) {
        return null;
    }

    public static ImVec2 imBezierCubicClosestPoint(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, ImVec2/*ref*/ p, int numSegments) {
        return null;
    }
}
