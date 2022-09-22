package imgui.api;

public final class ImVec4 {
    public float x, y, z, w;

    public ImVec4() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        w = 0.0f;
    }

    public ImVec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(ImVec4 src) {
        x = src.x;
        y = src.y;
        z = src.z;
        w = src.w;
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
}
