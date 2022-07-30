package imgui;

public final class ImDrawListSplitter {
    public int _Current;
    public int _Count;
    public ImVector<ImDrawChannel> _Channels = new ImVector<>();

    public void clear() {
        _Current = 0;
        _Count = 1;
    }

    public void split(ImDrawList drawList, int count) {

    }

    public void merge(ImDrawList drawList) {

    }

    public void setCurrentChannel(ImDrawList drawList, int channelIdx) {

    }
}
