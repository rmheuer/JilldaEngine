package imgui;

import static imgui.ImGuiMacros.*;

public final class ImColor {
    public ImVec4 value = new ImVec4();

    public ImColor() {}

    public ImColor(float r, float g, float b) { this(r, g, b, 1.0f); }
    public ImColor(float r, float g, float b, float a) {
        value = new ImVec4(r, g, b, a);
    }

    public ImColor(ImVec4 col) {
        value = col;
    }

    public ImColor(int r, int g, int b) { this(r, g, b, 255); }
    public ImColor(int r, int g, int b, int a) {
        float sc = 1.0f / 255.0f;
        value.x = (float) r * sc;
        value.y = (float) g * sc;
        value.z = (float) b * sc;
        value.w = (float) a * sc;
    }

    public ImColor(int rgba) {
        float sc = 1.0f / 255.0f;
        value.x = (float) ((rgba >>> IM_COL32_R_SHIFT) & 0xFF) * sc;
        value.y = (float) ((rgba >>> IM_COL32_G_SHIFT) & 0xFF) * sc;
        value.z = (float) ((rgba >>> IM_COL32_B_SHIFT) & 0xFF) * sc;
        value.w = (float) ((rgba >>> IM_COL32_A_SHIFT) & 0xFF) * sc;
    }

    public int toImU32() {
        return ImGui.colorConvertFloat4ToU32(value);
    }

    public ImVec4 toImVec4() {
        return value;
    }

    public void setHSV(float h, float s, float v) { setHSV(h, s, v, 1.0f); }
    public void setHSV(float h, float s, float v, float a) {
        float[] r = new float[1];
        float[] g = new float[1];
        float[] b = new float[1];
        ImGui.colorConvertHSVtoRGB(h, s, v, r, g, b);
        value.x = r[0];
        value.y = g[0];
        value.z = b[0];
        value.w = a;
    }

    public static ImColor hsv(float h, float s, float v) { return hsv(h, s, v, 1.0f); }
    public static ImColor hsv(float h, float s, float v, float a) {
        float[] r = new float[1];
        float[] g = new float[1];
        float[] b = new float[1];
        ImGui.colorConvertHSVtoRGB(h, s, v, r, g, b);
        return new ImColor(r[0], g[0], b[0], a);
    }
}
