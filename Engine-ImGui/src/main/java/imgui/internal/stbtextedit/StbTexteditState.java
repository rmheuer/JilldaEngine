package imgui.internal.stbtextedit;

public final class StbTexteditState {
    public int cursor;

    public int selectStart;
    public int selectEnd;

    public boolean insertMode;

    public int rowCountPerPage;

    public boolean cursorAtEndOfLine;
    public boolean initialized;
    public boolean hasPreferredX;
    public boolean singleLine;
    public float preferredX;
    public final StbUndoState undoState;

    public StbTexteditState(StbTextedit<?> stb) {
        undoState = new StbUndoState(stb);
    }
}
