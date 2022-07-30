package imgui.fn;

import imgui.ImDrawCmd;
import imgui.ImDrawList;

@FunctionalInterface
public interface ImDrawCallback {
    void call(ImDrawList parentList, ImDrawCmd cmd);
}
