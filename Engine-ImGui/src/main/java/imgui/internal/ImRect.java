package imgui.internal;

import imgui.api.ImVec2;
import imgui.api.ImVec4;

import static imgui.internal.ImGuiInternal.imClamp;
import static imgui.internal.ImGuiInternal.imMax;
import static imgui.internal.ImGuiInternal.imMin;
import static imgui.internal.ImGuiMacrosInternal.IM_FLOOR;

public final class ImRect {
    public final ImVec2 min;
    public final ImVec2 max;

    public ImRect() {
        min = new ImVec2(0.0f, 0.0f);
        max = new ImVec2(0.0f, 0.0f);
    }

    public ImRect(ImVec2/*ref*/ min, ImVec2/*ref*/ max) {
        this.min = new ImVec2(min);
        this.max = new ImVec2(max);
    }

    public ImRect(ImVec4/*ref*/ v) {
        min = new ImVec2(v.x, v.y);
        max = new ImVec2(v.z, v.w);
    }

    public ImRect(float x1, float y1, float x2, float y2) {
        min = new ImVec2(x1, y1);
        max = new ImVec2(x2, y2);
    }

    public ImVec2 getCenter() {
        return new ImVec2((min.x + max.x) * 0.5f, (min.y + max.y) * 0.5f);
    }

    public ImVec2 getSize() {
        return new ImVec2(max.x - min.x, max.y - min.y);
    }

    public float getWidth() {
        return max.x - min.x;
    }

    public float getHeight() {
        return max.y - min.y;
    }

    public float getArea() {
        return (max.x - min.x) * (max.y - min.y);
    }

    public ImVec2 getTL() {
        return new ImVec2(min);
    }

    public ImVec2 getTR() {
        return new ImVec2(max.x, min.y);
    }

    public ImVec2 getBL() {
        return new ImVec2(min.x, max.y);
    }

    public ImVec2 getBR() {
        return new ImVec2(max);
    }

    public boolean contains(ImVec2/*ref*/ p) {
        return p.x >= min.x && p.y >= min.y && p.x < max.x && p.y < max.y;
    }

    public boolean contains(ImRect/*ref*/ r) {
        return r.min.x >= min.x && r.min.y >= min.y && r.max.x <= max.x && r.max.y <= max.y;
    }

    public boolean overlaps(ImRect/*ref*/ r) {
        return r.min.y < max.y && r.max.y > min.y && r.min.x < max.x && r.max.x > min.x;
    }

    public void add(ImVec2/*ref*/ p) {
        if (min.x > p.x) min.x = p.x;
        if (min.y > p.y) min.y = p.y;
        if (max.x < p.x) max.x = p.x;
        if (max.y < p.y) max.y = p.y;
    }

    public void add(ImRect r) {
        if (min.x > r.min.x) min.x = r.min.x;
        if (min.y > r.min.y) min.y = r.min.y;
        if (max.x < r.max.x) max.x = r.max.x;
        if (max.y < r.max.y) max.y = r.max.y;
    }

    public void expand(float amount) {
        min.x -= amount;
        min.y -= amount;
        max.x += amount;
        max.y += amount;
    }

    public void expand(ImVec2/*ref*/ amount) {
        min.x -= amount.x;
        min.y -= amount.y;
        max.x += amount.x;
        max.y += amount.y;
    }

    public void translate(ImVec2/*ref*/ d) {
        min.x += d.x;
        min.y += d.y;
        max.x += d.x;
        max.y += d.y;
    }

    public void translateX(float dx) {
        min.x += dx;
        max.x += dx;
    }

    public void translateY(float dy) {
        min.y += dy;
        max.y += dy;
    }

    public void clipWith(ImRect/*ref*/ r) {
        min.set(imMax(min, r.min));
        max.set(imMin(max, r.max));
    }

    public void clipWithFull(ImRect/*ref*/ r) {
        min.set(imClamp(min, r.min, r.max));
        max.set(imClamp(max, r.min, r.max));
    }

    public void floor() {
        min.x = IM_FLOOR(min.x);
        min.y = IM_FLOOR(min.y);
        max.x = IM_FLOOR(max.x);
        max.y = IM_FLOOR(max.y);
    }

    public boolean isInverted() {
        return min.x > max.x || min.y > max.y;
    }

    public ImVec4 toVec4() {
        return new ImVec4(min.x, min.y, max.x, max.y);
    }
}
