package imgui.api;

import static imgui.api.ImGuiMacros.IM_ASSERT;

public final class ImVec2 {
    public float x, y;

    public ImVec2() {
        x = 0.0f;
        y = 0.0f;
    }

    public ImVec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Copy constructor
    public ImVec2(ImVec2 o) {
        x = o.x;
        y = o.y;
    }

    public float getComp(int idx) {
        IM_ASSERT(idx <= 1);

        if (idx == 0)
            return x;
        else
            return y;
    }

    public void setComp(int idx, float v) {
        IM_ASSERT(idx <= 1);

        if (idx == 0)
            x = v;
        else
            y = v;
    }

    public void set(ImVec2 src) {
        x = src.x;
        y = src.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
