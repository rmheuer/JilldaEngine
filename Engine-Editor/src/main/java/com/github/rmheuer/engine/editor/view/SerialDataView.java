package com.github.rmheuer.engine.editor.view;

import com.github.rmheuer.engine.core.serial.node.SerialArray;
import com.github.rmheuer.engine.core.serial.node.SerialBoolean;
import com.github.rmheuer.engine.core.serial.node.SerialByte;
import com.github.rmheuer.engine.core.serial.node.SerialChar;
import com.github.rmheuer.engine.core.serial.node.SerialDouble;
import com.github.rmheuer.engine.core.serial.node.SerialFloat;
import com.github.rmheuer.engine.core.serial.node.SerialInt;
import com.github.rmheuer.engine.core.serial.node.SerialLong;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.node.SerialReference;
import com.github.rmheuer.engine.core.serial.node.SerialShort;
import com.github.rmheuer.engine.core.serial.node.SerialString;
import com.github.rmheuer.engine.core.util.CheckedTypeSwitch;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiTableFlags;
import com.github.rmheuer.engine.gui.GuiTreeFlags;

public final class SerialDataView {
    private static void primitiveName(GuiRenderer r, String name) {
        r.tableNextColumn();
        r.text(name);
        r.tableNextColumn();
    }

    private static void showArray(GuiRenderer r, String name, SerialArray a, boolean root) {

    }

    private static void showBoolean(GuiRenderer r, String name, SerialBoolean b) {
        primitiveName(r, name);
        r.text(String.valueOf(b.getValue()));
    }

    private static void showByte(GuiRenderer r, String name, SerialByte b) {
        primitiveName(r, name);
        r.text(String.valueOf(b.getValue()));
    }

    private static void showChar(GuiRenderer r, String name, SerialChar c) {
        primitiveName(r, name);
        r.text(String.valueOf(c.getValue()));
    }

    private static void showDouble(GuiRenderer r, String name, SerialDouble d) {
        primitiveName(r, name);
        r.text(String.valueOf(d.getValue()));
    }

    private static void showFloat(GuiRenderer r, String name, SerialFloat f) {
        primitiveName(r, name);
        r.text(String.valueOf(f.getValue()));
    }

    private static void showInt(GuiRenderer r, String name, SerialInt i) {
        primitiveName(r, name);
        r.text(String.valueOf(i.getValue()));
    }

    private static void showLong(GuiRenderer r, String name, SerialLong l) {
        primitiveName(r, name);
        r.text(String.valueOf(l.getValue()));
    }

    private static boolean showObject(GuiRenderer r, String name, SerialObject o, boolean root) {
        int flags = GuiTreeFlags.BackgroundFillsAvailX;
        if (root)
            flags |= GuiTreeFlags.DefaultOpen;

        r.tableNextColumn();
        boolean open = r.pushTree(name, name, flags);
        r.tableNextColumn();

        boolean changed = false;
        if (open) {
            for (String key : o.keySet()) {
                changed |= showNode(r, key, o.getRaw(key), false);
            }
            r.popTree();
        }

        return changed;
    }

    private static void showReference(GuiRenderer r, String name, SerialReference ref) {

    }

    private static void showShort(GuiRenderer r, String name, SerialShort s) {
        primitiveName(r, name);
        r.text(String.valueOf(s.getValue()));
    }

    private static boolean showString(GuiRenderer r, String name, SerialString s) {
        primitiveName(r, name);

        StringBuilder builder = new StringBuilder(s.getValue());
        if (r.editString(builder, name)) {
            s.setValue(builder.toString());
            return true;
        }

        return false;
    }

    private static boolean showNode(GuiRenderer r, String name, SerialNode node, boolean root) {
        if (node == null) {
            primitiveName(r, name);
            r.text(null);
            return false;
        }

        final boolean[] changed = {false};
        CheckedTypeSwitch<SerialNode> s = new CheckedTypeSwitch<>(node);
        s.doCase(SerialArray.class,     (a) -> showArray    (r, name, a, root));
        s.doCase(SerialBoolean.class,   (b) -> showBoolean  (r, name, b));
        s.doCase(SerialByte.class,      (b) -> showByte     (r, name, b));
        s.doCase(SerialChar.class,      (c) -> showChar     (r, name, c));
        s.doCase(SerialDouble.class,    (d) -> showDouble   (r, name, d));
        s.doCase(SerialFloat.class,     (f) -> showFloat    (r, name, f));
        s.doCase(SerialInt.class,       (i) -> showInt      (r, name, i));
        s.doCase(SerialLong.class,      (l) -> showLong     (r, name, l));
        s.doCase(SerialObject.class,    (o) -> changed[0] = showObject(r, name, o, root));
        s.doCase(SerialReference.class, (f) -> showReference(r, name, f));
        s.doCase(SerialShort.class,     (h) -> showShort    (r, name, h));
        s.doCase(SerialString.class,    (t) -> changed[0] = showString(r, name, t));

        return changed[0];
    }

    public static boolean show(GuiRenderer r, String name, SerialNode node) {
        r.beginTableFlags(GuiTableFlags.NoBorder | GuiTableFlags.NoPadding, 2, 3);
        boolean changed = showNode(r, name, node, true);
        r.endTable();
        return changed;
    }

    private SerialDataView() {
        throw new AssertionError();
    }
}
