package imgui;

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
}
