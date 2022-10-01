package imgui.internal;

import imgui.api.ImVec2;

public final class ImVec2ih {
    public short x, y;

    public ImVec2ih() {
        x = 0;
        y = 0;
    }

    public ImVec2ih(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public ImVec2ih(ImVec2/*ref*/ rhs) {
        x = (short) rhs.x;
        y = (short) rhs.y;
    }
}
