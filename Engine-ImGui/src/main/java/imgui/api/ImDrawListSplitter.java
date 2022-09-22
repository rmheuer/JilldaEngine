package imgui.api;

import java.util.ArrayList;
import java.util.List;

public final class ImDrawListSplitter {
    public int current;
    public int count;
    public final List<ImDrawChannel> channels = new ArrayList<>();

    public ImDrawListSplitter() {}

    public void clear() {
        current = 0;
        count = 1;
    }

    public void split(ImDrawList/*ptr*/ drawList, int count) {

    }

    public void merge(ImDrawList/*ptr*/ drawList) {

    }

    public void setCurrentChannel(ImDrawList/*ptr*/ drawList, int channelIdx) {

    }
}
