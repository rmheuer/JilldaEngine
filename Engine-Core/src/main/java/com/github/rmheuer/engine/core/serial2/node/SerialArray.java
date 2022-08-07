package com.github.rmheuer.engine.core.serial2.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class SerialArray extends TraversableNode implements Iterable<SerialNode> {
    private final List<SerialNode> list;

    public SerialArray() {
        list = new ArrayList<>();
    }

    public void add(SerialNode node) {
        if (node == null)
            node = new SerialNull();

        int idx = list.size();
        list.add(node);
        node.setParent(this);
        node.setPathToken(Integer.toString(idx));
    }

    public void addByte(byte val) {
        add(new SerialByte(val));
    }

    public void addShort(short val) {
        add(new SerialShort(val));
    }

    public void addInt(int val) {
        add(new SerialInt(val));
    }

    public void addLong(long val) {
        add(new SerialLong(val));
    }

    public void addFloat(float val) {
        add(new SerialFloat(val));
    }

    public void addDouble(double val) {
        add(new SerialDouble(val));
    }

    public void addChar(char val) {
        add(new SerialChar(val));
    }

    public void addString(String val) {
        add(new SerialString(val));
    }

    public void addBoolean(boolean val) {
        add(new SerialBoolean(val));
    }

    public void addReference(SerialNode target) {
        add(new SerialRef(target));
    }

    public SerialNode get(int idx) {
        SerialNode node = getActual(idx);
        if (node instanceof SerialRef) {
            node = ((SerialRef) node).eval();
        }
        return node;
    }

    public SerialNode getActual(int idx) {
        return list.get(idx);
    }

    public SerialObject getObject(int idx) {
        return (SerialObject) get(idx);
    }

    public SerialArray getArray(int idx) {
        return (SerialArray) get(idx);
    }

    public NumericNode getNumber(int idx) {
        return (NumericNode) get(idx);
    }

    public TextualNode getText(int idx) {
        return (TextualNode) get(idx);
    }

    public String getString(int idx) {
        return getText(idx).getString();
    }

    public char getChar(int idx) {
        return getText(idx).getChar();
    }

    public byte getByte(int idx) {
        return getNumber(idx).getByte();
    }

    public short getShort(int idx) {
        return getNumber(idx).getShort();
    }

    public int getInt(int idx) {
        return getNumber(idx).getInt();
    }

    public long getLong(int idx) {
        return getNumber(idx).getLong();
    }

    public float getFloat(int idx) {
        return getNumber(idx).getFloat();
    }

    public double getDouble(int idx) {
        return getNumber(idx).getDouble();
    }

    public boolean getBoolean(int idx) {
        return ((SerialBoolean) get(idx)).getValue();
    }

    public int length() {
        return list.size();
    }

    @Override
    public SerialNode evalPathToken(String tok) {
        try {
            return list.get(Integer.parseInt(tok));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Iterator<SerialNode> iterator() {
        return list.iterator();
    }

    public Iterator<SerialNode> actualIterator() {
        List<SerialNode> actualList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            actualList.add(get(i));
        }
        return actualList.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        boolean comma = false;
        for (SerialNode node : list) {
            if (comma) builder.append(", ");
            else comma = true;

            builder.append(node);
        }
        builder.append("]");
        return builder.toString();
    }
}
