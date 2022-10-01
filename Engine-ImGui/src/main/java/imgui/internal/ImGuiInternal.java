package imgui.internal;

import imgui.api.ImGuiContext;
import imgui.api.ImVec2;
import imgui.api.ImVec4;

import static imgui.internal.ImGuiMacrosInternal.imFabs;

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

    public static int imAbs(int x) {
        return x < 0 ? -x : x;
    }

    public static float imAbs(float x) {
        return Math.abs(x);
    }

    public static double imAbs(double x) {
        return Math.abs(x);
    }

    public static float imSign(float x) {
        return (x < 0.0f) ? -1.0f : (x > 0.0f) ? 1.0f : 0.0f;
    }

    public static double imSign(double x) {
        return (x < 0.0) ? -1.0 : (x > 0.0) ? 1.0 : 0.0;
    }

    public static float imRsqrt(float x) {
        return 1.0f / (float) Math.sqrt(x);
    }

    public static double imRsqrt(double x) {
        return 1.0 / Math.sqrt(x);
    }

    public static float imMin(float lhs, float rhs) {
        return Math.min(lhs, rhs);
    }

    public static int imMin(int lhs, int rhs) {
        return Math.min(lhs, rhs);
    }

    public static float imMax(float lhs, float rhs) {
        return Math.max(lhs, rhs);
    }

    public static float imClamp(float v, float mn, float mx) {
        return (v < mn) ? mn : Math.min(v, mx);
    }

    public static int imClamp(int v, int mn, int mx) {
        return (v < mn) ? mn : Math.min(v, mx);
    }

    public static ImVec2 imMin(ImVec2/*ref*/ lhs, ImVec2/*ref*/ rhs) {
        return new ImVec2(Math.min(lhs.x, rhs.x), Math.min(lhs.y, rhs.y));
    }

    public static ImVec2 imMax(ImVec2/*ref*/ lhs, ImVec2/*ref*/ rhs) {
        return new ImVec2(Math.max(lhs.x, rhs.x), Math.max(lhs.y, rhs.y));
    }

    public static ImVec2 imClamp(ImVec2/*ref*/ v, ImVec2/*ref*/ mn, ImVec2 mx) {
        return new ImVec2(imClamp(v.x, mn.x, mx.x), imClamp(v.y, mn.y, mx.y));
    }

    public static ImVec2 imLerp(ImVec2/*ref*/ a, ImVec2/*ref*/ b, float t) {
        return new ImVec2(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t);
    }

    public static ImVec2 imLerp(ImVec2/*ref*/ a, ImVec2/*ref*/ b, ImVec2/*ref*/ t) {
        return new ImVec2(a.x + (b.x - a.x) * t.x, a.y + (b.y - a.y) * t.y);
    }

    public static ImVec4 imLerp(ImVec4/*ref*/ a, ImVec4/*ref*/ b, float t) {
        return new ImVec4(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t,
                a.z + (b.z - a.z) * t,
                a.w + (b.w - a.w) * t
        );
    }

    public static float imSaturate(float f) {
        return (f < 0.0f) ? 0.0f : Math.min(f, 1.0f);
    }

    public static float imLengthSqr(ImVec2/*ref*/ lhs) {
        return (lhs.x * lhs.x) + (lhs.y * lhs.y);
    }

    public static float imLengthSqr(ImVec4/*ref*/ lhs) {
        return (lhs.x * lhs.x) + (lhs.y * lhs.y) + (lhs.z * lhs.z) + (lhs.w * lhs.w);
    }

    public static float imInvLength(ImVec2 lhs, float failValue) {
        float d = (lhs.x * lhs.x) + (lhs.y * lhs.y);
        if (d > 0.0f)
            return imRsqrt(d);
        return failValue;
    }

    public static float imFloor(float f) {
        return (int) f;
    }

    public static float imFloorSigned(float f) {
        return (f >= 0 || (int) f == f) ? (int) f : (int) f - 1;
    }

    public static ImVec2 imFloor(ImVec2/*ref*/ v) {
        return new ImVec2(imFloor(v.x), imFloor(v.y));
    }

    public static ImVec2 imFloorSigned(ImVec2/*ref*/ v) {
        return new ImVec2(imFloorSigned(v.x), imFloorSigned(v.y));
    }

    public static int imModPositive(int a, int b) {
        return (a + b) % b;
    }

    public static float imDot(ImVec2/*ref*/ a, ImVec2/*ref*/ b) {
        return a.x * b.x + a.y * b.y;
    }

    public static ImVec2 imRotate(ImVec2/*ref*/ v, float cosA, float sinA) {
        return new ImVec2(v.x * cosA - v.y * sinA, v.x * sinA + v.y * cosA);
    }

    public static float imLinearSweep(float current, float target, float speed) {
        if (current < target)
            return imMin(current + speed, target);
        if (current > target)
            return imMax(current - speed, target);
        return current;
    }

    public static ImVec2 imMul(ImVec2/*ref*/ lhs, ImVec2/*ref*/ rhs) {
        return new ImVec2(lhs.x * rhs.x, lhs.y * rhs.y);
    }

    public static boolean imIsFloatAboveGuaranteedIntegerPrecision(float f) {
        return f <= -16777216 || f >= 16777216;
    }

    public static ImVec2 imBezierCubicCalc(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, float t) {
        return null;
    }

    public static ImVec2 imBezierCubicClosestPoint(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, ImVec2/*ref*/ p, int numSegments) {
        return null;
    }

    public static ImVec2 imBezierCubicClosestPointCasteljau(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, ImVec2/*ref*/ p, float tessTol) {
        return null;
    }

    public static ImVec2 imBezierQuadraticCalc(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, float t) {
        return null;
    }

    public static ImVec2 imLineClosestPoint(ImVec2/*ref*/ a, ImVec2/*ref*/ b, ImVec2/*ref*/ p) {
        return null;
    }

    public static boolean imTriangleContainsPoint(ImVec2/*ref*/ a, ImVec2/*ref*/ b, ImVec2/*ref*/ c, ImVec2/*ref*/ p) {
        return false;
    }

    public static ImVec2 imTriangleClosestPoint(ImVec2/*ref*/ a, ImVec2/*ref*/ b, ImVec2/*ref*/ c, ImVec2/*ref*/ p) {
        return null;
    }

    public static void imTriangleBarycentricCoords(ImVec2/*ref*/ a, ImVec2/*ref*/ b, ImVec2/*ref*/ c, ImVec2/*ref*/ p, float[] outU, float[] outV, float[] outW) {

    }

    public static float imTriangleArea(ImVec2/*ref*/ a, ImVec2/*ref*/ b, ImVec2/*ref*/ c) {
        return imFabs((a.x * (b.y - c.y)) + (b.x * (c.y - a.y)) + (c.x * (a.y - b.y))) * 0.5f;
    }

    public static int imGetDirQuadrantFromDelta(float dx, float dy) {
        return 0;
    }

    public static boolean imBitArrayTestBit(int[] arr, int n) {
        int mask = 1 << (n & 31);
        return (arr[n >> 5] & mask) != 0;
    }

    public static void imBitArrayClearBit(int[] arr, int n) {
        int mask = 1 << (n & 31);
        arr[n >> 5] &= ~mask;
    }

    public static void imBitArraySetBit(int[] arr, int n) {
        int mask = 1 << (n & 31);
        arr[n >> 5] |= mask;
    }

    public static void imBitArraySetBitRange(int[] arr, int n, int n2) {
        n2--;
        while (n <= n2) {
            int aMod = (n & 31);
            int bMod = (n2 > (n | 31) ? 31 : (n2 & 31)) + 1;
            int mask = (int) ((1L << bMod) - 1) & ~(int) ((1L << aMod) - 1);
            arr[n >> 5] |= mask;
            n = (n + 32) & ~31;
        }
    }
}
