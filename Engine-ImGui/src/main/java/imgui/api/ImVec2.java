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

    public ImVec2 mul(float rhs) {
        return new ImVec2(x * rhs, y * rhs);
    }

    public ImVec2 div(float rhs) {
        return new ImVec2(x / rhs, y / rhs);
    }

    public ImVec2 add(ImVec2/*ref*/ rhs) {
        return new ImVec2(x + rhs.x, y + rhs.y);
    }

    public ImVec2 sub(ImVec2/*ref*/ rhs) {
        return new ImVec2(x - rhs.x, y - rhs.y);
    }

    public ImVec2 mul(ImVec2/*ref*/ rhs) {
        return new ImVec2(x * rhs.x, y * rhs.y);
    }

    public ImVec2 div(ImVec2/*ref*/ rhs) {
        return new ImVec2(x / rhs.x, y / rhs.y);
    }

    public ImVec2 mulEq(float rhs) {
        x *= rhs;
        y *= rhs;
        return this;
    }

    public ImVec2 divEq(float rhs) {
        x /= rhs;
        y /= rhs;
        return this;
    }

    public ImVec2 addEq(ImVec2/*ref*/ rhs) {
        x += rhs.x;
        y += rhs.y;
        return this;
    }

    public ImVec2 subEq(ImVec2/*ref*/ rhs) {
        x -= rhs.x;
        y -= rhs.y;
        return this;
    }

    public ImVec2 mulEq(ImVec2/*ref*/ rhs) {
        x *= rhs.x;
        y *= rhs.y;
        return this;
    }

    public ImVec2 divEq(ImVec2/*ref*/ rhs) {
        x /= rhs.x;
        y /= rhs.y;
        return this;
    }
}
