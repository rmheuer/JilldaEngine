package imgui;

public final class ImVec4 {
    public float x;
    public float y;
    public float z;
    public float w;

    public ImVec4() {
        x = 0;
        y = 0;
        z = 0;
        w = 0;
    }

    public ImVec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
}
