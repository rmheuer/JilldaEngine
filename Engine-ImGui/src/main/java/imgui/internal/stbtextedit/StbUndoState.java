package imgui.internal.stbtextedit;

public final class StbUndoState {
    public final StbUndoRecord[] undoRec;
    public final char[] undoChar;
    public short undoPoint, redoPoint;
    public int undoCharPoint, redoCharPoint;

    public StbUndoState(StbTextedit<?> stb) {
        undoRec = new StbUndoRecord[stb.undoStateCount];
        undoChar = new char[stb.undoCharCount];
    }
}
