package imgui.internal;

import imgui.api.ImDrawList;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class ImDrawDataBuilder {
    private final List/*<ImDrawList*>*/[] layers = new List[2];

    public void clear() {
        for (List layer : layers) {
            layer.clear();
        }
    }

    public int getDrawListCount() {
        int count = 0;
        for (List layer : layers) {
            count += layer.size();
        }
        return count;
    }

    public void flattenIntoSingleLayer() {

    }

    public List<ImDrawList/*ptr*/> getLayer(int i) {
        return (List<ImDrawList>) layers[i];
    }

    public void setLayer(int i, List<ImDrawList/*ptr*/> layer) {
        layers[i] = layer;
    }
}
