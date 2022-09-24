package imgui.internal.stbtextedit;

public final class StbUndoRecord {
    public int where;
    public int insertLength;
    public int deleteLength;
    public int charStorage;

    public StbUndoRecord copy() {
        StbUndoRecord n = new StbUndoRecord();
        n.where = where;
        n.insertLength = insertLength;
        n.deleteLength = deleteLength;
        n.charStorage = charStorage;
        return n;
    }
}
