package imgui.internal.flag;

import static imgui.api.flag.ImGuiDataType.COUNT;

public final class ImGuiDataTypePrivate {
    public static final int String = COUNT + 1;
    public static final int Pointer = COUNT + 2;
    public static final int ID = COUNT + 3;

    private ImGuiDataTypePrivate() {
        throw new AssertionError();
    }
}
