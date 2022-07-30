package imgui;

import java.util.Objects;

public final class ImVec2 {
    public float x;
    public float y;

    public ImVec2() {
        x = 0.0f;
        y = 0.0f;
    }

    public ImVec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float get(int idx) {
        switch (idx) {
            case 0: return x;
            case 1: return y;
        }
        throw new IndexOutOfBoundsException(String.valueOf(idx));
    }

    public void set(int idx, float val) {
        switch (idx) {
            case 0: x = val; break;
            case 1: y = val; break;
            default:
                throw new IndexOutOfBoundsException(String.valueOf(idx));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImVec2 imVec2 = (ImVec2) o;
        return Float.compare(imVec2.x, x) == 0 &&
                Float.compare(imVec2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
