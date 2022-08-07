package com.github.rmheuer.engine.core.serial2.node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SerialObject extends TraversableNode {
    private final Map<String, SerialNode> map;

    public SerialObject() {
        map = new HashMap<>();
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

    public String getString(String key) {
        return ((TextualNode) get(key)).getString();
    }

    public void put(String key, SerialNode value) {
        SerialNode removed = map.put(key, value);
        if (removed != null)
            removed.setParent(null);
        value.setParent(this);
        value.setPathToken(key);
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
