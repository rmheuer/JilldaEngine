package com.github.rmheuer.engine.core.serial2.json;

import com.github.rmheuer.engine.core.serial2.node.NumericNode;
import com.github.rmheuer.engine.core.serial2.node.SerialArray;
import com.github.rmheuer.engine.core.serial2.node.SerialBoolean;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.node.SerialNull;
import com.github.rmheuer.engine.core.serial2.node.SerialObject;
import com.github.rmheuer.engine.core.serial2.node.SerialRef;
import com.github.rmheuer.engine.core.serial2.node.TextualNode;
import com.github.rmheuer.engine.core.util.CheckedTypeSwitch;
import com.github.rmheuer.engine.core.util.StringUtils;

import java.io.IOException;
import java.io.Writer;

public final class JsonEncoder {
    private static final String INDENT = "  ";

    private final Writer out;
    private String newLine = "\n";

    public JsonEncoder(Writer out) {
        this.out = out;
    }

    private void encodeRef(SerialRef ref) throws IOException {
        out.append("{\"$ref\": \"");
        out.append(ref.getRelativePathTo(ref.getTarget()));
        out.append("\"}");
    }

    private void encodeObject(SerialObject obj) throws IOException {
        out.append("{");
        indent();
        boolean comma = false;
        for (String key : obj.keySet()) {
            SerialNode value = obj.getActual(key);

            if (comma) out.append(", ");
            else comma = true;

            newLine();
            out.append(StringUtils.quoteString(key));
            out.append(": ");
            encode(value);
        }
        unindent();
        if (comma) newLine();
        out.append("}");
    }

    private void encodeArray(SerialArray arr) throws IOException {
        out.append("[");
        indent();
        boolean comma = false;
        for (int i = 0; i < arr.length(); i++) {
            SerialNode node = arr.getActual(i);

            if (comma) out.append(", ");
            else comma = true;

            newLine();
            encode(node);
        }
        unindent();
        if (comma) newLine();
        out.append("]");
    }

    private void encodeString(TextualNode str) throws IOException {
        out.append(StringUtils.quoteString(str.getString()));
    }

    private void encodeBoolean(SerialBoolean bool) throws IOException {
        out.append(bool.getValue() ? "true" : "false");
    }

    private void encodeNumber(NumericNode node) throws IOException {
        if (node.isWhole()) {
            out.append(Long.toString(node.getLong()));
        } else {
            out.append(Double.toString(node.getDouble()));
        }
    }

    public void encode(SerialNode node) throws IOException {
        CheckedTypeSwitch<SerialNode> s = new CheckedTypeSwitch<>(node);
        s.doCase(SerialRef.class, this::encodeRef);
        s.doCase(SerialObject.class, this::encodeObject);
        s.doCase(SerialArray.class, this::encodeArray);
        s.doCase(TextualNode.class, this::encodeString);
        s.doCase(SerialBoolean.class, this::encodeBoolean);
        s.doCase(SerialNull.class, (n) -> out.append("null"));
        s.doCase(NumericNode.class, this::encodeNumber);
    }

    private void newLine() throws IOException {
        out.append(newLine);
    }

    private void indent() {
        newLine += INDENT;
    }

    private void unindent() {
        newLine = newLine.substring(0, newLine.length() - INDENT.length());
    }
}
