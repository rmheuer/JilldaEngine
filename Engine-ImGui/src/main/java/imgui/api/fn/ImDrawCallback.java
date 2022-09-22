package imgui.api.fn;

import imgui.api.ImDrawCmd;
import imgui.api.ImDrawList;

@FunctionalInterface
public interface ImDrawCallback {
    void call(ImDrawList parentList, ImDrawCmd cmd);

    ImDrawCallback resetRenderState = (parentList, cmd) -> {};
}
