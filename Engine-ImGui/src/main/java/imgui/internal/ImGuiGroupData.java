package imgui.internal;

import imgui.api.ImVec2;

public final class ImGuiGroupData {
    public int windowID;
    public final ImVec2 backupCursorPos = new ImVec2();
    public final ImVec2 backupCursorMaxPos = new ImVec2();
    public final ImVec1 backupIndent = new ImVec1();
    public final ImVec1 backupGroupOffset = new ImVec1();
    public final ImVec2 backupCurrLineSize = new ImVec2();
    public float backupCurrLineTextBaseOffset;
    public int backupActiveIdIsAlive;
    public boolean backupActiveIdPreviousFrameIsAlive;
    public boolean backupHoveredIdIsAlive;
    public boolean emitItem;
}
