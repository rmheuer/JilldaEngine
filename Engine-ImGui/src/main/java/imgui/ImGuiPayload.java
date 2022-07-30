package imgui;

import java.util.Arrays;

public final class ImGuiPayload {
    public Object data;

    public int sourceId;
    public int sourceParentId;
    public int dataFrameCount;
    public char[] dataType = new char[32 + 1];
    public boolean preview;
    public boolean delivery;

    public ImGuiPayload() {
        clear();
    }

    public void clear() {
        sourceId = sourceParentId = 0;
        data = null;
        Arrays.fill(dataType, (char) 0);
        dataFrameCount = -1;
        preview = delivery = false;
    }

    public boolean isDataType(String type) {
        if (dataFrameCount == -1)
            return false;
        char[] typeChars = type.toCharArray();
        for (int i = 0; i < typeChars.length; i++) {
            if (dataType[i] != typeChars[i])
                return false;
        }
        return dataType[typeChars.length] == 0;
    }

    public boolean isPreview() {
        return preview;
    }

    public boolean isDelivery() {
        return delivery;
    }
}
