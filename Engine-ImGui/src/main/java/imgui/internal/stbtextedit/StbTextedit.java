package imgui.internal.stbtextedit;

import imgui.internal.stbtextedit.fn.StbTexteditDeleteCharsFn;
import imgui.internal.stbtextedit.fn.StbTexteditGetCharFn;
import imgui.internal.stbtextedit.fn.StbTexteditGetWidthFn;
import imgui.internal.stbtextedit.fn.StbTexteditInsertCharsFn;
import imgui.internal.stbtextedit.fn.StbTexteditIsSpaceFn;
import imgui.internal.stbtextedit.fn.StbTexteditKeyToTextFn;
import imgui.internal.stbtextedit.fn.StbTexteditLayOutRowFn;
import imgui.internal.stbtextedit.fn.StbTexteditMoveWordLeftFn;
import imgui.internal.stbtextedit.fn.StbTexteditMoveWordRightFn;
import imgui.internal.stbtextedit.fn.StbTexteditStringLenFn;

import static imgui.api.ImGuiMacros.IM_ASSERT;

public final class StbTextedit<StringType> {
    public int locateCoord(StringType str, float x, float y) {
        StbTexteditRow r = new StbTexteditRow();
        int n = stringLen(str);
        float baseY = 0, prevX;
        int i = 0, k;

        r.x0 = r.x1 = 0;
        r.yMin = r.yMax = 0;
        r.numChars = 0;

        while (i < n) {
            layOutRow(r, str, i);
            if (r.numChars <= 0)
                return n;

            if (i == 0 && y < baseY + r.yMin)
                return 0;

            if (y < baseY + r.yMax)
                break;

            i += r.numChars;
            baseY += r.baselineYDelta;
        }

        if (i >= n)
            return n;

        if (x < r.x0)
            return i;

        if (x < r.x1) {
            prevX = r.x0;
            for (k = 0; k < r.numChars; k++) {
                float w = getWidth(str, i, k);
                if (x < prevX + w) {
                    if (x < prevX + w / 2)
                        return k + i;
                    else
                        return k + i + 1;
                }
                prevX += w;
            }
        }

        if (getChar(str, i + r.numChars - 1) == newLine)
            return i + r.numChars - 1;
        else
            return i + r.numChars;
    }

    public void click(StringType str, StbTexteditState state, float x, float y) {
        if (state.singleLine) {
            StbTexteditRow r = new StbTexteditRow();
            layOutRow(r, str, 0);
            y = r.yMin;
        }

        state.cursor = locateCoord(str, x, y);
        state.selectStart = state.cursor;
        state.selectEnd = state.cursor;
        state.hasPreferredX = false;
    }

    public void drag(StringType str, StbTexteditState state, float x, float y) {
        int p = 0;

        if (state.singleLine) {
            StbTexteditRow r = new StbTexteditRow();
            layOutRow(r, str, 0);
            y = r.yMin;
        }

        if (state.selectStart == state.selectEnd)
            state.selectStart = state.cursor;

        p = locateCoord(str, x, y);
        state.cursor = state.selectEnd = p;
    }

    public void findCharPos(StbFindState find, StringType str, int n, boolean singleLine) {
        StbTexteditRow r = new StbTexteditRow();
        int prevStart = 0;
        int z = stringLen(str);
        int i = 0, first;

        if (n == z) {
            if (singleLine) {
                layOutRow(r, str, 0);
                find.y = 0;
                find.firstChar = 0;
                find.length = z;
                find.height = r.yMax - r.yMin;
                find.x = r.x1;
            } else {
                find.y = 0;
                find.x = 0;
                find.height = 1;
                while (i < z) {
                    layOutRow(r, str, i);
                    prevStart = i;
                    i += r.numChars;
                }
                find.firstChar = i;
                find.length = 0;
                find.prevFirst = prevStart;
            }
            return;
        }

        find.y = 0;

        for (;;) {
            layOutRow(r, str, i);
            if (n < i + r.numChars)
                break;
            prevStart = i;
            i += r.numChars;
            find.y += r.baselineYDelta;
        }

        find.firstChar = first = i;
        find.length = r.numChars;
        find.height = r.yMax - r.yMin;
        find.prevFirst = prevStart;

        find.x = r.x0;
        for (i = 0; first + i < n; i++)
            find.x += getWidth(str, first, i);
    }

    public static boolean hasSelection(StbTexteditState s) {
        return s.selectStart != s.selectEnd;
    }

    public void clamp(StringType str, StbTexteditState state) {
        int n = stringLen(str);
        if (hasSelection(state)) {
            if (state.selectStart > n) state.selectStart = n;
            if (state.selectEnd   > n) state.selectEnd = n;
            if (state.selectStart == state.selectEnd)
                state.cursor = state.selectStart;
        }
        if (state.cursor > n) state.cursor = n;
    }

    public void delete(StringType str, StbTexteditState state, int where, int len) {
        makeUndoDelete(str, state, where, len);
        deleteChars(str, where, len);
        state.hasPreferredX = false;
    }
    
    public void deleteSelection(StringType str, StbTexteditState state) {
        clamp(str, state);
        if (hasSelection(state)) {
            if (state.selectStart < state.selectEnd) {
                delete(str, state, state.selectStart, state.selectEnd - state.selectStart);
                state.selectEnd = state.cursor = state.selectStart;
            } else {
                delete(str, state, state.selectEnd, state.selectStart - state.selectEnd);
                state.selectStart = state.cursor = state.selectEnd;
            }
            state.hasPreferredX = false;
        }
    }
    
    public void sortSelection(StbTexteditState state) {
        if (state.selectEnd < state.selectStart) {
            int temp = state.selectEnd;
            state.selectEnd = state.selectStart;
            state.selectStart = temp;
        }
    }

    public void moveToFirst(StbTexteditState state) {
        if (hasSelection(state)) {
            sortSelection(state);
            state.cursor = state.selectStart;
            state.selectEnd = state.selectStart;
            state.hasPreferredX = false;
        }
    }

    public void moveToLast(StringType str, StbTexteditState state) {
        if (hasSelection(state)) {
            sortSelection(state);
            clamp(str, state);
            state.cursor = state.selectEnd;
            state.selectStart = state.selectEnd;
            state.hasPreferredX = false;
        }
    }

    public void prepSelectionAtCursor(StbTexteditState state) {
        if (!hasSelection(state))
            state.selectStart = state.selectEnd = state.cursor;
        else
            state.cursor = state.selectEnd;
    }

    public boolean cut(StringType str, StbTexteditState state) {
        if (hasSelection(state)) {
            deleteSelection(str, state);
            state.hasPreferredX = false;
            return true;
        }
        return false;
    }

    public boolean pasteInternal(StringType str, StbTexteditState state, String text) {
        clamp(str, state);
        deleteSelection(str, state);
        if (insertChars(str, state.cursor, text)) {
            makeUndoInsert(state, state.cursor, text.length());
            state.cursor += text.length();
            state.hasPreferredX = false;
            return true;
        }
        return false;
    }

    public void key(StringType str, StbTexteditState state, int key) {
        while (true) {
            if (kInsert != null && key == kInsert) {
                state.insertMode = !state.insertMode;
            } else if (key == kUndo) {
                undo(str, state);
                state.hasPreferredX = false;
            } else if (key == kRedo) {
                redo(str, state);
                state.hasPreferredX = false;
            } else if (key == kLeft) {
                if (hasSelection(state))
                    moveToFirst(state);
                else if (state.cursor > 0)
                    --state.cursor;
                state.hasPreferredX = false;
            } else if (key == kRight) {
                if (hasSelection(state))
                    moveToLast(str, state);
                else
                    ++state.cursor;
                clamp(str, state);
                state.hasPreferredX = false;
            } else if (key == (kLeft | kShift)) {
                clamp(str, state);
                prepSelectionAtCursor(state);
                if (state.selectEnd > 0)
                    --state.selectEnd;
                state.cursor = state.selectEnd;
                state.hasPreferredX = false;
            } else if (moveWordLeft != null && key == kWordLeft) {
                if (hasSelection(state))
                    moveToFirst(state);
                else {
                    state.cursor = moveWordLeft(str, state.cursor);
                    clamp(str, state);
                }
            } else if (moveWordLeft != null && key == (kWordLeft | kShift)) {
                if (!hasSelection(state))
                    prepSelectionAtCursor(state);

                state.cursor = moveWordLeft(str, state.cursor);
                state.selectEnd = state.cursor;

                clamp(str, state);
            } else if (moveWordRight != null && key == kWordRight) {
                if (hasSelection(state))
                    moveToLast(str, state);
                else {
                    state.cursor = moveWordRight(str, state.cursor);
                    clamp(str, state);
                }
            } else if (moveWordRight != null && key == (kWordRight | kShift)) {
                if (!hasSelection(state))
                    prepSelectionAtCursor(state);

                state.cursor = moveWordRight(str, state.cursor);
                state.selectEnd = state.cursor;

                clamp(str, state);
            } else if (key == (kRight | kShift)) {
                prepSelectionAtCursor(state);
                ++state.selectEnd;
                clamp(str, state);
                state.cursor = state.selectEnd;
                state.hasPreferredX = false;
            } else if (key == kDown
                    || key == (kDown | kShift)
                    || key == kPgDown
                    || key == (kPgDown | kShift)) {
                StbFindState find = new StbFindState();
                StbTexteditRow row = new StbTexteditRow();
                int i, j;
                boolean sel = (key & kShift) != 0;
                boolean isPage = (key & ~kShift) == kPgDown;
                int rowCount = isPage ? state.rowCountPerPage : 1;

                if (!isPage && state.singleLine) {
                    key = kRight | (key & kShift);
                    continue;
                }

                if (sel)
                    prepSelectionAtCursor(state);
                else if (hasSelection(state))
                    moveToLast(str, state);

                clamp(str, state);
                findCharPos(find, str, state.cursor, state.singleLine);

                for (j = 0; j < rowCount; ++j) {
                    float x, goalX = state.hasPreferredX ? state.preferredX : find.x;
                    int start = find.firstChar + find.length;

                    if (find.length == 0)
                        break;

                    if (getChar(str, find.firstChar + find.length - 1) != newLine)
                        break;

                    state.cursor = start;
                    layOutRow(row, str, state.cursor);
                    x = row.x0;
                    for (i = 0; i < row.numChars; ++i) {
                        float dx = getWidth(str, start, i);
                        if (getWidthNewLine != null && dx == getWidthNewLine)
                            break;
                        x += dx;
                        if (x > goalX)
                            break;
                        ++state.cursor;
                    }
                    clamp(str, state);

                    state.hasPreferredX = true;
                    state.preferredX = goalX;

                    if (sel)
                        state.selectEnd = state.cursor;

                    find.firstChar = find.firstChar + find.length;
                    find.length = row.numChars;
                }
            } else if (key == kUp
                    || key == (kUp | kShift)
                    || key == kPgUp
                    || key == (kPgUp | kShift)) {
                StbFindState find = new StbFindState();
                StbTexteditRow row = new StbTexteditRow();
                int i, j, prevScan;
                boolean sel = (key & kShift) != 0;
                boolean isPage = (key & ~kShift) == kPgUp;
                int rowCount = isPage ? state.rowCountPerPage : 1;

                if (!isPage && state.singleLine) {
                    key = kLeft | (key & kShift);
                    continue;
                }

                if (sel)
                    prepSelectionAtCursor(state);
                else if (hasSelection(state))
                    moveToFirst(state);

                clamp(str, state);
                findCharPos(find, str, state.cursor, state.singleLine);

                for (j = 0; j < rowCount; j++) {
                    float x, goalX = state.hasPreferredX ? state.preferredX : find.x;

                    if (find.prevFirst == find.firstChar)
                        break;

                    state.cursor = find.prevFirst;
                    layOutRow(row, str, state.cursor);
                    x = row.x0;
                    for (i = 0; i < row.numChars; i++) {
                        float dx = getWidth(str, find.prevFirst, i);
                        if (getWidthNewLine != null && dx == getWidthNewLine) {
                            break;
                        }
                        x += dx;
                        if (x > goalX)
                            break;
                        ++state.cursor;
                    }
                    clamp(str, state);

                    state.hasPreferredX = true;
                    state.preferredX = goalX;

                    if (sel)
                        state.selectEnd = state.cursor;

                    prevScan = find.prevFirst > 0 ? find.prevFirst - 1 : 0;
                    while (prevScan > 0 && getChar(str, prevScan - 1) != newLine)
                        --prevScan;
                    find.firstChar = find.prevFirst;
                    find.prevFirst = prevScan;
                }
            } else if (key == kDelete || key == (kDelete | kShift)) {
                if (hasSelection(state))
                    deleteSelection(str, state);
                else {
                    int n = stringLen(str);
                    if (state.cursor < n)
                        delete(str, state, state.cursor, 1);
                }
                state.hasPreferredX = false;
            } else if (key == kBackspace || key == (kBackspace | kShift)) {
                if (hasSelection(state))
                    deleteSelection(str, state);
                else {
                    clamp(str, state);
                    if (state.cursor > 0) {
                        delete(str, state, state.cursor - 1, 1);
                        --state.cursor;
                    }
                }
                state.hasPreferredX = false;
            } else if ((kTextStart2 != null && key == kTextStart2) || key == kTextStart) {
                state.cursor = state.selectStart = state.selectEnd = 0;
                state.hasPreferredX = false;
            } else if ((kTextEnd2 != null && key == kTextEnd2) || key == kTextEnd) {
                state.cursor = stringLen(str);
                state.selectStart = state.selectEnd = 0;
                state.hasPreferredX = false;
            } else if ((kTextStart2 != null && key == (kTextStart2 | kShift)) || key == (kTextStart | kShift)) {
                prepSelectionAtCursor(state);
                state.cursor = state.selectEnd = 0;
                state.hasPreferredX = false;
            } else if ((kTextEnd2 != null && key == (kTextEnd2 | kShift)) || key == (kTextEnd | kShift)) {
                prepSelectionAtCursor(state);
                state.cursor = state.selectEnd = stringLen(str);
                state.hasPreferredX = false;
            } else if ((kLineStart2 != null && key == kLineStart2) || key == kLineStart) {
                clamp(str, state);
                moveToFirst(state);
                if (state.singleLine)
                    state.cursor = 0;
                else while (state.cursor > 0 && getChar(str, state.cursor - 1) != newLine)
                    --state.cursor;
                state.hasPreferredX = false;
            } else if ((kLineEnd2 != null && key == kLineEnd2) || key == kLineEnd) {
                int n = stringLen(str);
                clamp(str, state);
                moveToFirst(state);
                if (state.singleLine)
                    state.cursor = n;
                else while (state.cursor < n && getChar(str, state.cursor) != newLine)
                    ++state.cursor;
                state.hasPreferredX = false;
            } else if ((kLineStart2 != null && key == (kLineStart2 | kShift)) || key == (kLineStart | kShift)) {
                clamp(str, state);
                prepSelectionAtCursor(state);
                if (state.singleLine)
                    state.cursor = 0;
                else while (state.cursor > 0 && getChar(str, state.cursor - 1) != newLine)
                    --state.cursor;
                state.selectEnd = state.cursor;
                state.hasPreferredX = false;
            } else if ((kLineEnd2 != null && key == (kLineEnd2 | kShift)) || key == (kLineEnd | kShift)) {
                int n = stringLen(str);
                clamp(str, state);
                prepSelectionAtCursor(state);
                if (state.singleLine)
                    state.cursor = n;
                else while (state.cursor < n && getChar(str, state.cursor) != newLine)
                    ++state.cursor;
                state.selectEnd = state.cursor;
                state.hasPreferredX = false;
            } else {
                int c = keyToText(key);
                if (c > 0) {
                    char ch = (char) c;

                    if (c != '\n' || !state.singleLine) {
                        if (state.insertMode && !hasSelection(state) && state.cursor < stringLen(str)) {
                            makeUndoReplace(str, state, state.cursor, 1, 1);
                            deleteChars(str, state.cursor, 1);
                            if (insertChars(str, state.cursor, String.valueOf(ch))) {
                                ++state.cursor;
                                state.hasPreferredX = false;
                            }
                        } else {
                            deleteSelection(str, state);
                            if (insertChars(str, state.cursor, String.valueOf(ch))) {
                                makeUndoInsert(state, state.cursor, 1);
                                ++state.cursor;
                                state.hasPreferredX = false;
                            }
                        }
                    }
                }
            }
            break;
        }
    }

    public void flushRedo(StbUndoState state) {
        state.redoPoint = (short) undoStateCount;
        state.redoCharPoint = undoCharCount;
    }

    public void discardUndo(StbUndoState state) {
        if (state.undoPoint > 0) {
            if (state.undoRec[0].charStorage >= 0) {
                int n = state.undoRec[0].insertLength, i;
                state.undoCharPoint -= n;
                System.arraycopy(state.undoChar, n, state.undoChar, 0, state.undoCharPoint);
                for (i = 0; i < state.undoPoint; i++)
                    if (state.undoRec[i].charStorage >= 0)
                        state.undoRec[i].charStorage -= n;
            }
            --state.undoPoint;
            System.arraycopy(state.undoRec, 1, state.undoRec, 0, state.undoPoint);

            // Reference is duplicated here, make it unique again
            state.undoRec[state.undoPoint] = state.undoRec[state.undoPoint].copy();
        }
    }

    public void discardRedo(StbUndoState state) {
        int k = undoStateCount - 1;

        if (state.redoPoint <= k) {
            if (state.undoRec[k].charStorage >= 0) {
                int n = state.undoRec[k].insertLength, i;
                state.redoCharPoint += n;
                System.arraycopy(state.undoChar, state.redoCharPoint - n, state.undoChar, state.redoCharPoint, undoCharCount - state.redoCharPoint);
                for (i = state.redoPoint; i < k; i++)
                    if (state.undoRec[i].charStorage >= 0)
                        state.undoRec[i].charStorage += n;
            }

            int moveSize = undoStateCount - state.redoPoint - 1;
            IM_ASSERT(state.redoPoint >= 0);
            IM_ASSERT(state.redoPoint + 1 + moveSize <= state.undoRec.length);
            System.arraycopy(state.undoRec, state.redoPoint, state.undoRec, state.redoPoint + 1, moveSize);

            int dupIdx = state.redoPoint;
            state.undoRec[dupIdx] = state.undoRec[dupIdx].copy();

            ++state.redoPoint;
        }
    }

    public StbUndoRecord createUndoRecord(StbUndoState state, int numChars) {
        flushRedo(state);

        if (state.undoPoint == undoStateCount)
            discardUndo(state);

        if (numChars > undoCharCount) {
            state.undoPoint = 0;
            state.undoCharPoint = 0;
            return null;
        }

        while (state.undoCharPoint + numChars > undoCharCount)
            discardUndo(state);

        return state.undoRec[state.undoPoint++];
    }

    // Changed to return index into state.undoChar instead of char*, null is -1
    public int createUndo(StbUndoState state, int pos, int insertLen, int deleteLen) {
        StbUndoRecord r = createUndoRecord(state, insertLen);
        if (r == null)
            return -1;

        r.where = pos;
        r.insertLength = insertLen;
        r.deleteLength = deleteLen;

        if (insertLen == 0) {
            r.charStorage = -1;
            return -1;
        } else {
            r.charStorage = state.undoCharPoint;
            state.undoCharPoint += insertLen;
            return r.charStorage;
        }
    }

    public void undo(StringType str, StbTexteditState state) {
        StbUndoState s = state.undoState;
        StbUndoRecord u, r;
        if (s.undoPoint == 0)
            return;

        u = s.undoRec[s.undoPoint - 1].copy();
        r = s.undoRec[s.redoPoint - 1];
        r.charStorage = -1;

        r.insertLength = u.deleteLength;
        r.deleteLength = u.insertLength;
        r.where = u.where;

        if (u.deleteLength != 0) {
            if (s.undoCharPoint + u.deleteLength >= undoCharCount) {
                r.insertLength = 0;
            } else {
                int i;

                while (s.undoCharPoint + u.deleteLength > s.redoCharPoint) {
                    if (s.redoPoint == undoStateCount)
                        return;
                    discardRedo(s);
                }
                r = s.undoRec[s.redoPoint - 1];

                r.charStorage = s.redoCharPoint - u.deleteLength;
                s.redoCharPoint = s.redoCharPoint - u.deleteLength;

                for (i = 0; i < u.deleteLength; i++)
                    s.undoChar[r.charStorage + i] = getChar(str, u.where + i);
            }

            deleteChars(str, u.where, u.deleteLength);
        }

        if (u.insertLength != 0) {
            char[] subBuffer = new char[u.insertLength];
            System.arraycopy(s.undoChar, u.charStorage, subBuffer, 0, u.insertLength);
            insertChars(str, u.where, new String(subBuffer));
            s.undoCharPoint -= u.insertLength;
        }

        state.cursor = u.where + u.insertLength;

        s.undoPoint--;
        s.redoPoint--;
    }

    public void redo(StringType str, StbTexteditState state) {
        StbUndoState s = state.undoState;
        StbUndoRecord u, r;
        if (s.redoPoint == undoStateCount)
            return;

        u = s.undoRec[s.undoPoint];
        r = s.undoRec[s.redoPoint].copy();

        u.deleteLength = r.insertLength;
        u.insertLength = r.deleteLength;
        u.where = r.where;
        u.charStorage = -1;

        if (r.deleteLength != 0) {
            if (s.undoCharPoint + u.insertLength > s.redoCharPoint) {
                u.insertLength = 0;
                u.deleteLength = 0;
            } else {
                int i;
                u.charStorage = s.undoCharPoint;
                s.undoCharPoint = s.undoCharPoint + u.insertLength;

                for (i = 0; i < u.insertLength; i++)
                    s.undoChar[u.charStorage + i] = getChar(str, u.where + i);
            }

            deleteChars(str, r.where, r.deleteLength);
        }

        if (r.insertLength != 0) {
            char[] subBuffer = new char[r.insertLength];
            System.arraycopy(s.undoChar, r.charStorage, subBuffer, 0, r.insertLength);
            insertChars(str, r.where, new String(subBuffer));
            s.redoCharPoint += r.insertLength;
        }

        s.undoPoint++;
        s.redoPoint++;
    }

    public void makeUndoDelete(StringType str, StbTexteditState state, int where, int length) {
        int i;
        int idx = createUndo(state.undoState, where, length, 0);
        if (idx != -1) {
            for (i = 0; i < length; i++)
                state.undoState.undoChar[idx + i] = getChar(str, where + i);
        }
    }

    public void makeUndoInsert(StbTexteditState state, int where, int length) {
        createUndo(state.undoState, where, 0, length);
    }

    public void makeUndoReplace(StringType str, StbTexteditState state, int where, int oldLength, int newLength) {
        int i;
        int idx = createUndo(state.undoState, where, oldLength, newLength);
        if (idx != -1) {
            for (i = 0; i < oldLength; i++)
                state.undoState.undoChar[idx + i] = getChar(str, where + i);
        }
    }

    public void clearState(StbTexteditState state, boolean isSingleLine) {
        state.undoState.undoPoint = 0;
        state.undoState.undoCharPoint = 0;
        state.undoState.redoPoint = (short) undoStateCount;
        state.undoState.redoCharPoint = undoCharCount;
        state.selectEnd = state.selectStart = 0;
        state.cursor = 0;
        state.hasPreferredX = false;
        state.preferredX = 0;
        state.cursorAtEndOfLine = false;
        state.initialized = true;
        state.singleLine = isSingleLine;
        state.insertMode = false;
        state.rowCountPerPage = 0;
    }

    public void initializeState(StbTexteditState state, boolean isSingleLine) {
        clearState(state, isSingleLine);
    }

    public boolean paste(StringType str, StbTexteditState state, String text) {
        return pasteInternal(str, state, text);
    }

    // ----- Context -----

    private boolean isNotWordBoundary(StringType str, int idx) {
        return idx > 0 && (!isSpace(getChar(str, idx - 1)) || isSpace(getChar(str, idx)));
    }

    // Set to moveWordLeft if isSpace is set
    private int defaultMoveToWordPrevious(StringType str, int c) {
        --c;
        while (c >= 0 && isNotWordBoundary(str, c))
            --c;

        if (c < 0)
            c = 0;

        return c;
    }

    // Set to moveWordRight if isSpace is set
    private int defaultMoveToWordNext(StringType str, int c) {
        int len = stringLen(str);
        ++c;
        while (c < len && isNotWordBoundary(str, c))
            ++c;

        if (c > len)
            c = len;

        return c;
    }

    public static final class Builder<StringType> {
        // Required
        private int undoStateCount = 99;
        private int undoCharCount = 999;
        private StbTexteditStringLenFn<StringType> stringLen;
        private StbTexteditLayOutRowFn<StringType> layOutRow;
        private StbTexteditGetWidthFn<StringType> getWidth;
        private StbTexteditKeyToTextFn keyToText;
        private StbTexteditGetCharFn<StringType> getChar;
        private char newLine;
        private StbTexteditDeleteCharsFn<StringType> deleteChars;
        private StbTexteditInsertCharsFn<StringType> insertChars;

        // Optional
        private StbTexteditIsSpaceFn isSpace;
        private StbTexteditMoveWordLeftFn<StringType> moveWordLeft;
        private StbTexteditMoveWordRightFn<StringType> moveWordRight;
        private Float getWidthNewLine;

        // Keys
        private Integer kInsert;
        private int kUndo;
        private int kRedo;
        private int kLeft;
        private int kRight;
        private int kShift;
        private Integer kWordLeft;
        private Integer kWordRight;
        private int kDown;
        private int kPgDown;
        private int kUp;
        private int kPgUp;
        private int kDelete;
        private int kBackspace;
        private int kTextStart;
        private Integer kTextStart2;
        private int kTextEnd;
        private Integer kTextEnd2;
        private int kLineEnd;
        private Integer kLineEnd2;
        private int kLineStart;
        private Integer kLineStart2;

        public StbTextedit<StringType> build() {
            return new StbTextedit<StringType>(undoStateCount, undoCharCount, stringLen, layOutRow, getWidth, keyToText, getChar, newLine, deleteChars, insertChars, isSpace, moveWordLeft, moveWordRight, getWidthNewLine, kInsert, kUndo, kRedo, kLeft, kRight, kShift, kWordLeft, kWordRight, kDown, kPgDown, kUp, kPgUp, kDelete, kBackspace, kTextStart, kTextStart2, kTextEnd, kTextEnd2, kLineEnd, kLineEnd2, kLineStart, kLineStart2);
        }

        public Builder<StringType> setUndoStateCount(int undoStateCount) {
            this.undoStateCount = undoStateCount;
            return this;
        }

        public Builder<StringType> setUndoCharCount(int undoCharCount) {
            this.undoCharCount = undoCharCount;
            return this;
        }

        public Builder<StringType> setStringLen(StbTexteditStringLenFn<StringType> stringLen) {
            this.stringLen = stringLen;
            return this;
        }

        public Builder<StringType> setLayOutRow(StbTexteditLayOutRowFn<StringType> layOutRow) {
            this.layOutRow = layOutRow;
            return this;
        }

        public Builder<StringType> setGetWidth(StbTexteditGetWidthFn<StringType> getWidth) {
            this.getWidth = getWidth;
            return this;
        }

        public Builder<StringType> setKeyToText(StbTexteditKeyToTextFn keyToText) {
            this.keyToText = keyToText;
            return this;
        }

        public Builder<StringType> setGetChar(StbTexteditGetCharFn<StringType> getChar) {
            this.getChar = getChar;
            return this;
        }

        public Builder<StringType> setNewLine(char newLine) {
            this.newLine = newLine;
            return this;
        }

        public Builder<StringType> setDeleteChars(StbTexteditDeleteCharsFn<StringType> deleteChars) {
            this.deleteChars = deleteChars;
            return this;
        }

        public Builder<StringType> setInsertChars(StbTexteditInsertCharsFn<StringType> insertChars) {
            this.insertChars = insertChars;
            return this;
        }

        public Builder<StringType> setIsSpace(StbTexteditIsSpaceFn isSpace) {
            this.isSpace = isSpace;
            return this;
        }

        public Builder<StringType> setMoveWordLeft(StbTexteditMoveWordLeftFn<StringType> moveWordLeft) {
            this.moveWordLeft = moveWordLeft;
            return this;
        }

        public Builder<StringType> setMoveWordRight(StbTexteditMoveWordRightFn<StringType> moveWordRight) {
            this.moveWordRight = moveWordRight;
            return this;
        }

        public Builder<StringType> setGetWidthNewLine(Float getWidthNewLine) {
            this.getWidthNewLine = getWidthNewLine;
            return this;
        }

        public Builder<StringType> setkInsert(Integer kInsert) {
            this.kInsert = kInsert;
            return this;
        }

        public Builder<StringType> setkUndo(int kUndo) {
            this.kUndo = kUndo;
            return this;
        }

        public Builder<StringType> setkRedo(int kRedo) {
            this.kRedo = kRedo;
            return this;
        }

        public Builder<StringType> setkLeft(int kLeft) {
            this.kLeft = kLeft;
            return this;
        }

        public Builder<StringType> setkRight(int kRight) {
            this.kRight = kRight;
            return this;
        }

        public Builder<StringType> setkShift(int kShift) {
            this.kShift = kShift;
            return this;
        }

        public Builder<StringType> setkWordLeft(Integer kWordLeft) {
            this.kWordLeft = kWordLeft;
            return this;
        }

        public Builder<StringType> setkWordRight(Integer kWordRight) {
            this.kWordRight = kWordRight;
            return this;
        }

        public Builder<StringType> setkDown(int kDown) {
            this.kDown = kDown;
            return this;
        }

        public Builder<StringType> setkPgDown(int kPgDown) {
            this.kPgDown = kPgDown;
            return this;
        }

        public Builder<StringType> setkUp(int kUp) {
            this.kUp = kUp;
            return this;
        }

        public Builder<StringType> setkPgUp(int kPgUp) {
            this.kPgUp = kPgUp;
            return this;
        }

        public Builder<StringType> setkDelete(int kDelete) {
            this.kDelete = kDelete;
            return this;
        }

        public Builder<StringType> setkBackspace(int kBackspace) {
            this.kBackspace = kBackspace;
            return this;
        }

        public Builder<StringType> setkTextStart(int kTextStart) {
            this.kTextStart = kTextStart;
            return this;
        }

        public Builder<StringType> setkTextStart2(Integer kTextStart2) {
            this.kTextStart2 = kTextStart2;
            return this;
        }

        public Builder<StringType> setkTextEnd(int kTextEnd) {
            this.kTextEnd = kTextEnd;
            return this;
        }

        public Builder<StringType> setkTextEnd2(Integer kTextEnd2) {
            this.kTextEnd2 = kTextEnd2;
            return this;
        }

        public Builder<StringType> setkLineEnd(int kLineEnd) {
            this.kLineEnd = kLineEnd;
            return this;
        }

        public Builder<StringType> setkLineEnd2(Integer kLineEnd2) {
            this.kLineEnd2 = kLineEnd2;
            return this;
        }

        public Builder<StringType> setkLineStart(int kLineStart) {
            this.kLineStart = kLineStart;
            return this;
        }

        public Builder<StringType> setkLineStart2(Integer kLineStart2) {
            this.kLineStart2 = kLineStart2;
            return this;
        }
    }

    // Required
    public final int undoStateCount;
    public final int undoCharCount;
    private final StbTexteditStringLenFn<StringType> stringLen;
    private final StbTexteditLayOutRowFn<StringType> layOutRow;
    private final StbTexteditGetWidthFn<StringType> getWidth;
    private final StbTexteditKeyToTextFn keyToText;
    private final StbTexteditGetCharFn<StringType> getChar;
    private final char newLine;
    private final StbTexteditDeleteCharsFn<StringType> deleteChars;
    private final StbTexteditInsertCharsFn<StringType> insertChars;

    // Optional
    private final StbTexteditIsSpaceFn isSpace;
    private StbTexteditMoveWordLeftFn<StringType> moveWordLeft;
    private StbTexteditMoveWordRightFn<StringType> moveWordRight;
    private final Float getWidthNewLine;

    // Keys
    private final Integer kInsert;
    private final int kUndo;
    private final int kRedo;
    private final int kLeft;
    private final int kRight;
    private final int kShift;
    private final Integer kWordLeft;
    private final Integer kWordRight;
    private final int kDown;
    private final int kPgDown;
    private final int kUp;
    private final int kPgUp;
    private final int kDelete;
    private final int kBackspace;
    private final int kTextStart;
    private final Integer kTextStart2;
    private final int kTextEnd;
    private final Integer kTextEnd2;
    private final int kLineEnd;
    private final Integer kLineEnd2;
    private final int kLineStart;
    private final Integer kLineStart2;

    public StbTextedit(int undoStateCount, int undoCharCount, StbTexteditStringLenFn<StringType> stringLen, StbTexteditLayOutRowFn<StringType> layOutRow, StbTexteditGetWidthFn<StringType> getWidth, StbTexteditKeyToTextFn keyToText, StbTexteditGetCharFn<StringType> getChar, char newLine, StbTexteditDeleteCharsFn<StringType> deleteChars, StbTexteditInsertCharsFn<StringType> insertChars, StbTexteditIsSpaceFn isSpace, StbTexteditMoveWordLeftFn<StringType> moveWordLeft, StbTexteditMoveWordRightFn<StringType> moveWordRight, Float getWidthNewLine, Integer kInsert, int kUndo, int kRedo, int kLeft, int kRight, int kShift, Integer kWordLeft, Integer kWordRight, int kDown, int kPgDown, int kUp, int kPgUp, int kDelete, int kBackspace, int kTextStart, Integer kTextStart2, int kTextEnd, Integer kTextEnd2, int kLineEnd, Integer kLineEnd2, int kLineStart, Integer kLineStart2) {
        this.undoStateCount = undoStateCount;
        this.undoCharCount = undoCharCount;
        this.stringLen = stringLen;
        this.layOutRow = layOutRow;
        this.getWidth = getWidth;
        this.keyToText = keyToText;
        this.getChar = getChar;
        this.newLine = newLine;
        this.deleteChars = deleteChars;
        this.insertChars = insertChars;
        this.isSpace = isSpace;
        this.moveWordLeft = moveWordLeft;
        this.moveWordRight = moveWordRight;
        this.getWidthNewLine = getWidthNewLine;
        this.kInsert = kInsert;
        this.kUndo = kUndo;
        this.kRedo = kRedo;
        this.kLeft = kLeft;
        this.kRight = kRight;
        this.kShift = kShift;
        this.kWordLeft = kWordLeft;
        this.kWordRight = kWordRight;
        this.kDown = kDown;
        this.kPgDown = kPgDown;
        this.kUp = kUp;
        this.kPgUp = kPgUp;
        this.kDelete = kDelete;
        this.kBackspace = kBackspace;
        this.kTextStart = kTextStart;
        this.kTextStart2 = kTextStart2;
        this.kTextEnd = kTextEnd;
        this.kTextEnd2 = kTextEnd2;
        this.kLineEnd = kLineEnd;
        this.kLineEnd2 = kLineEnd2;
        this.kLineStart = kLineStart;
        this.kLineStart2 = kLineStart2;

        if (isSpace != null) {
            if (moveWordLeft == null)
                this.moveWordLeft = this::defaultMoveToWordPrevious;
            if (moveWordRight == null)
                this.moveWordRight = this::defaultMoveToWordNext;
        }
    }

    public int stringLen(StringType str) {
        return stringLen.get(str);
    }

    public void layOutRow(StbTexteditRow r, StringType obj, int n) {
        layOutRow.layOut(r, obj, n);
    }

    public float getWidth(StringType obj, int n, int i) {
        return getWidth.get(obj, n, i);
    }

    public int keyToText(int key) {
        return keyToText.convert(key);
    }

    public char getChar(StringType obj, int i) {
        return getChar.get(obj, i);
    }

    public void deleteChars(StringType obj, int i, int n) {
        deleteChars.delete(obj, i, n);
    }

    public boolean insertChars(StringType obj, int i, String str) {
        return insertChars.insert(obj, i, str);
    }

    public boolean isSpace(char c) {
        return isSpace.check(c);
    }

    public int moveWordLeft(StringType str, int c) {
        return moveWordLeft.move(str, c);
    }

    public int moveWordRight(StringType str, int c) {
        return moveWordRight.move(str, c);
    }
}

/*
STB Licences:

------------------------------------------------------------------------------
This software is available under 2 licenses -- choose whichever you prefer.
------------------------------------------------------------------------------
ALTERNATIVE A - MIT License
Copyright (c) 2017 Sean Barrett
Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
------------------------------------------------------------------------------
ALTERNATIVE B - Public Domain (www.unlicense.org)
This is free and unencumbered software released into the public domain.
Anyone is free to copy, modify, publish, use, compile, sell, or distribute this
software, either in source code form or as a compiled binary, for any purpose,
commercial or non-commercial, and by any means.
In jurisdictions that recognize copyright laws, the author or authors of this
software dedicate any and all copyright interest in the software to the public
domain. We make this dedication for the benefit of the public at large and to
the detriment of our heirs and successors. We intend this dedication to be an
overt act of relinquishment in perpetuity of all present and future rights to
this software under copyright law.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
------------------------------------------------------------------------------
*/
