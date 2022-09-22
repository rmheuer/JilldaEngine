package imgui.api;

import java.util.ArrayList;
import java.util.List;

public final class ImDrawChannel {
    public final List<ImDrawCmd> cmdBuffer = new ArrayList<>();
    public final List<Integer> idxBuffer = new ArrayList<>();
}
