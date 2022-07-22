package com.github.rmheuer.engine.core.serial.codec.json;

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
import com.github.rmheuer.engine.core.util.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.IdentityHashMap;
import java.util.Map;

public final class JsonEncoder {
    private static final String INDENT = "  ";

    private final Writer out;
    private final Map<SerialNode, Integer> referenceIds;
    private String newLine;

    public JsonEncoder(Writer out) {
        this.out = out;
        referenceIds = new IdentityHashMap<>();
        newLine = "\n";
    }

    private int preprocess(SerialNode node, int nextRefId) {
        if (node instanceof SerialReference) {
            SerialReference ref = (SerialReference) node;
            referenceIds.put(ref.getReference(), nextRefId++);
        } else if (node != null) {
            for (SerialNode child : node.getChildren()) {
                nextRefId = preprocess(child, nextRefId);
            }
        }
        return nextRefId;
    }

    private void encodeNull() throws IOException {
        out.write("null");
    }

    private void encodeByte(SerialByte b) throws IOException {
        out.write(String.valueOf(b));
    }

    private void encodeShort(SerialShort s) throws IOException {
        out.write(String.valueOf(s));
    }

    private void encodeInt(SerialInt i) throws IOException {
        out.write(String.valueOf(i));
    }

    private void encodeLong(SerialLong l) throws IOException {
        out.write(String.valueOf(l));
    }

    private void encodeFloat(SerialFloat f) throws IOException {
        out.write(String.valueOf(f));
    }

    private void encodeDouble(SerialDouble d) throws IOException {
        out.write(String.valueOf(d));
    }

    private void encodeBoolean(SerialBoolean b) throws IOException {
        out.write(b.getValue() ? "true" : "false");
    }

    private void encodeChar(SerialChar c) throws IOException {
        out.write(StringUtils.quoteString(String.valueOf(c.getValue())));
    }

    private void encodeString(SerialString s) throws IOException {
        out.write(StringUtils.quoteString(s.getValue()));
    }

    private void indent() {
        newLine += INDENT;
    }

    private void unindent() {
        newLine = newLine.substring(0, newLine.length() - INDENT.length());
    }

    private void encodeObject(SerialObject o) throws IOException {
        out.write('{');
        indent();
        boolean first = true;
        boolean content = false;
        for (String key : o.keySet()) {
            if (!first)
                out.write(",");
            first = false;

            out.write(newLine);

            out.write(StringUtils.quoteString(key));
            out.write(": ");
            encodeNode(o.getRaw(key));

            content = true;
        }
        unindent();
        if (content)
            out.write(newLine);
        out.write('}');
    }

    private void encodeArray(SerialArray arr) throws IOException {
        out.write('[');
        indent();

        boolean first = true;
        boolean content = false;
        for (int i = 0; i < arr.size(); i++) {
            if (!first)
                out.write(",");
            first = false;

            out.write(newLine);
            encodeNode(arr.getRaw(i));

            content = true;
        }

        unindent();
        if (content)
            out.write(newLine);
        out.write(']');
    }

    private void encodeReference(SerialReference ref) throws IOException {
        out.write('{');
        indent();
        out.write(newLine + JsonSerialCodec.REFERENCE_KEY_NAME + ": ");
        out.write(String.valueOf(referenceIds.get(ref.getReference())));
        unindent();
        out.write(newLine + "}");
    }

    private void encodeNode(SerialNode node) throws IOException {
        boolean referenced = referenceIds.containsKey(node);

        if (referenced) {
            out.write('{');
            indent();
            out.write(newLine + JsonSerialCodec.REFERENCE_ID_KEY_NAME + ": ");
            out.write(String.valueOf(referenceIds.get(node)));
            out.write("," + newLine + JsonSerialCodec.REFERENCE_ID_VALUE_NAME + ": ");
        }

        if (node == null) {
            encodeNull();
        } else {
            new CheckedTypeSwitch<>(node)
                    .doCase(SerialByte.class,      this::encodeByte)
                    .doCase(SerialShort.class,     this::encodeShort)
                    .doCase(SerialInt.class,       this::encodeInt)
                    .doCase(SerialLong.class,      this::encodeLong)
                    .doCase(SerialFloat.class,     this::encodeFloat)
                    .doCase(SerialDouble.class,    this::encodeDouble)
                    .doCase(SerialBoolean.class,   this::encodeBoolean)
                    .doCase(SerialChar.class,      this::encodeChar)
                    .doCase(SerialString.class,    this::encodeString)
                    .doCase(SerialObject.class,    this::encodeObject)
                    .doCase(SerialArray.class,     this::encodeArray)
                    .doCase(SerialReference.class, this::encodeReference);
        }

        if (referenced) {
            unindent();
            out.write(newLine + "}");
        }
    }

    public void encode(SerialNode node) throws IOException {
        preprocess(node, 1);
        encodeNode(node);
    }

    public void close() throws IOException {
        out.close();
    }
}
