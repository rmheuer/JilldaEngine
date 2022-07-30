package imgui.fn;

public final class ImGuiInputTextCallbackData {
    public int eventFlag;
    public int flags;
    public Object userData;

    public char eventChar;
    public int eventKey;
    public char[] buf;
    public int bufTextLen;
    public int bufSize;
    public boolean bufDirty;
    public int cursorPos;
    public int selectionStart;
    public int selectionEnd;

    public ImGuiInputTextCallbackData() {}
    public void deleteChars(int pos, int bytesCount) {}
    public void insertChars(int pos, String text) {}
    public void selectAll() { selectionStart = 0; selectionEnd = bufTextLen; }
    public void clearSelection() { selectionStart = selectionEnd = bufTextLen; }
    public boolean hasSelection() { return selectionStart != selectionEnd; }
}
