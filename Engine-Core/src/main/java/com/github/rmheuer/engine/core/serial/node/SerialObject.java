package com.github.rmheuer.engine.core.serial.node;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class SerialObject extends SerialNode {
    private final Map<String, SerialNode> children;

    public SerialObject() {
	children = new LinkedHashMap<>();
    }

    public SerialNode get(String key) {
	SerialNode node = children.get(key);
	if (node instanceof SerialReference) {
	    return ((SerialReference) node).evaluate();
	} else {
	    return node;
	}
    }

    public SerialNode getRaw(String key) {
	return children.get(key);
    }

    public SerialObject getSerialObject(String key) {
	return (SerialObject) get(key);
    }

    public SerialArray getSerialArray(String key) {
	return (SerialArray) get(key);
    }

    public SerialNumber getSerialNumber(String key) {
	return (SerialNumber) get(key);
    }

    public Number getNumber(String key) {
	return getSerialNumber(key).getAsNumber();
    }
    
    public byte getByte(String key) {
	return getSerialNumber(key).getAsByte();
    }

    public short getShort(String key) {
	return getSerialNumber(key).getAsShort();
    }

    public int getInt(String key) {
	return getSerialNumber(key).getAsInt();
    }

    public long getLong(String key) {
	return getSerialNumber(key).getAsLong();
    }

    public float getFloat(String key) {
	return getSerialNumber(key).getAsFloat();
    }

    public double getDouble(String key) {
	return getSerialNumber(key).getAsDouble();
    }

    public boolean getBoolean(String key) {
	return ((SerialBoolean) get(key)).getValue();
    }

    public char getChar(String key) {
	return ((SerialChar) get(key)).getValue();
    }

    public String getString(String key) {
	return ((SerialString) get(key)).getValue();
    }

    public void put(String key, SerialNode value) {
	if (value == null) {
	    remove(key);
	} else {
	    SerialNode old = children.put(key, value);
	    value.setParent(this);
	    if (old != null) {
		old.setParent(null);
	    }
	}
    }

    public void putReference(String key, SerialNode ref) {
	put(key, new SerialReference(ref));
    }

    public void putByte(String key, byte value) {
	put(key, new SerialByte(value));
    }

    public void putShort(String key, short value) {
	put(key, new SerialShort(value));
    }

    public void putInt(String key, int value) {
	put(key, new SerialInt(value));
    }

    public void putLong(String key, long value) {
	put(key, new SerialLong(value));
    }

    public void putFloat(String key, float value) {
	put(key, new SerialFloat(value));
    }

    public void putDouble(String key, double value) {
	put(key, new SerialDouble(value));
    }

    public void putBoolean(String key, boolean value) {
	put(key, new SerialBoolean(value));
    }

    public void putChar(String key, char value) {
	put(key, new SerialChar(value));
    }

    public void putString(String key, String value) {
	put(key, new SerialString(value));
    }

    public void remove(String key) {
	SerialNode removed = children.remove(key);
	if (removed != null) {
	    removed.setParent(null);
	}
    }

    public boolean containsKey(String key) {
	return children.containsKey(key);
    }

    public int size() {
	return children.size();
    }

    public boolean isEmpty() {
	return children.isEmpty();
    }

    public Set<String> keySet() {
	return new HashSet<>(children.keySet());
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder("{");
	boolean comma = false;
	for (Map.Entry<String, SerialNode> entry : children.entrySet()) {
	    if (comma) builder.append(", ");
	    else comma = true;

	    builder.append(entry.getKey());
	    builder.append(": ");
	    builder.append(entry.getValue());
	}
	builder.append("}");
	return builder.toString();
    }

    @Override
    public Collection<SerialNode> getChildren() {
	return Collections.unmodifiableCollection(children.values());
    }
}
