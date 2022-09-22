package imgui.api;

public final class ImGuiPayload {
    public Object data;

    public int sourceId;
    public int sourceParentId;
    public int dataFrameCount;
    public String dataType;
    public boolean preview;
    public boolean delivery;

    public ImGuiPayload() {
        clear();
    }

    public void clear() {
        sourceId = sourceParentId = 0;
        data = null;
        dataType = "";
        dataFrameCount = -1;
        preview = delivery = false;
    }

    public boolean isDataType(String type) {
        return dataFrameCount != -1 && type.equals(dataType);
    }

    public boolean isPreview() {
        return preview;
    }

    public boolean isDelivery() {
        return delivery;
    }
}
