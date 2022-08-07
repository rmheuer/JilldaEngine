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
        int idx = list.size();
        list.add(node);
        node.setParent(this);
        node.setPathToken(Integer.toString(idx));
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
