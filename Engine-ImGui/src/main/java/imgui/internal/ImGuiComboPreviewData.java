package imgui.internal;

import imgui.api.ImVec2;

public final class ImGuiComboPreviewData {
    public final ImRect previewRect = new ImRect();
    public final ImVec2 backupCursorPos = new ImVec2();
    public final ImVec2 backupCursorMaxPos = new ImVec2();
    public final ImVec2 backupCursorPosPrevLine = new ImVec2();
    public float backupPrevLineTextBaseOffset;
    public int backupLayout;
}
