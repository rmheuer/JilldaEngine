package imgui.internal;

import imgui.internal.stbtextedit.StbTexteditState;

import static imgui.impl.ImGuiImpl.IMSTB;
import static imgui.internal.ImGuiInternal.imMin;

public final class ImGuiInputTextState {
    public int id;
    public StringBuilder text;
    public boolean textAIsValid;
    public int bufCapacityA;
    public float scrollX;
    public StbTexteditState stb;
    public float cursorAnim;
    public boolean cursorFollow;
    public boolean selectedAllMouseLock;
    public boolean edited;
    public int flags;

    public void clearText() {
        text.delete(0, text.length());
    }

    public int getUndoAvailCount() {
        return stb.undoState.undoPoint;
    }

    public int getRedoAvailCount() {
        return IMSTB.undoStateCount - stb.undoState.redoPoint;
    }

    public void onKeyPressed(int key) {

    }

    public void cursorAnimReset() {
        cursorAnim = -0.30f;
    }

    public void cursorClamp() {
        int len = text.length();
        stb.cursor = imMin(stb.cursor, len);
        stb.selectStart = imMin(stb.selectStart, len);
        stb.selectEnd = imMin(stb.selectEnd, len);
    }

    public boolean hasSelection() {
        return stb.selectStart != stb.selectEnd;
    }

    public void clearSelection() {
        stb.selectStart = stb.selectEnd = stb.cursor;
    }

    public int getCursorPos() {
        return stb.cursor;
    }

    public int getSelectionStart() {
        return stb.selectStart;
    }

    public int getSelectionEnd() {
        return stb.selectEnd;
    }

    public void selectAll() {
        stb.selectStart = 0;
        stb.cursor = stb.selectEnd = text.length();
        stb.hasPreferredX = false;
    }
}
