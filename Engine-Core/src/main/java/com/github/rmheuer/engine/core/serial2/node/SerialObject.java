package com.github.rmheuer.engine.core.serial2.node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SerialObject extends TraversableNode {
    private final Map<String, SerialNode> map;

    public SerialObject() {
        map = new HashMap<>();
    }

    public boolean isNull(String key) {
        return get(key) instanceof SerialNull;
    }

    public SerialNode get(String key) {
        SerialNode actual = getActual(key);
        if (actual instanceof SerialRef) {
            actual = ((SerialRef) actual).eval();
        }
        return actual;
    }

    public SerialNode getActual(String key) {
        return map.get(key);
    }

    public SerialObject getObject(String key) {
        return (SerialObject) get(key);
    }

    public SerialArray getArray(String key) {
        return (SerialArray) get(key);
    }

    public NumericNode getNumber(String key) {
        return (NumericNode) get(key);
    }

    public TextualNode getText(String key) {
        return (TextualNode) get(key);
    }

    public String getString(String key) {
        return getText(key).getString();
    }

    public char getChar(String key) {
        return getText(key).getChar();
    }

    public byte getByte(String key) {
        return getNumber(key).getByte();
    }

    public short getShort(String key) {
        return getNumber(key).getShort();
    }

    public int getInt(String key) {
        return getNumber(key).getInt();
    }

    public long getLong(String key) {
        return getNumber(key).getLong();
    }

    public float getFloat(String key) {
        return getNumber(key).getFloat();
    }

    public double getDouble(String key) {
        return getNumber(key).getDouble();
    }

    public boolean getBoolean(String key) {
        return ((SerialBoolean) get(key)).getValue();
    }

    public void put(String key, SerialNode value) {
        if (value == null)
            value = new SerialNull();

        SerialNode removed = map.put(key, value);
        if (removed != null)
            removed.setParent(null);
        value.setParent(this);
        value.setPathToken(key);
    }

    public void putByte(String key, byte val) {
        put(key, new SerialByte(val));
    }

    public void putShort(String key, short val) {
        put(key, new SerialShort(val));
    }

    public void putInt(String key, int val) {
        put(key, new SerialInt(val));
    }

    public void putLong(String key, long val) {
        put(key, new SerialLong(val));
    }

    public void putFloat(String key, float val) {
        put(key, new SerialFloat(val));
    }

    public void putDouble(String key, double val) {
        put(key, new SerialDouble(val));
    }

    public void putChar(String key, char val) {
        put(key, new SerialChar(val));
    }

    public void putString(String key, String val) {
        put(key, new SerialString(val));
    }

    public void putBoolean(String key, boolean val) {
        put(key, new SerialBoolean(val));
    }

    public void putReference(String key, SerialNode target) {
        put(key, new SerialRef(target));
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public SerialNode evalPathToken(String tok) {
        return map.get(tok);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        boolean comma = false;
        for (Map.Entry<String, SerialNode> entry : map.entrySet()) {
            if (comma) builder.append(", ");
            else comma = true;

            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue());
        }
        builder.append("}");
        return builder.toString();
    }
}
