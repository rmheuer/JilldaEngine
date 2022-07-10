package com.github.rmheuer.engine.core.serial.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class SerialArray extends SerialNode implements Iterable<SerialNode> {
    private final List<SerialNode> children;

    public SerialArray() {
	children = new ArrayList<>();
    }

    public SerialNode get(int index) {
	SerialNode node = children.get(index);
	if (node instanceof SerialReference) {
	    return ((SerialReference) node).evaluate();
	} else {
	    return node;
	}
    }

    public SerialNode getRaw(int index) {
	return children.get(index);
    }

    public SerialObject getSerialObject(int index) {
	return (SerialObject) get(index);
    }

    public SerialArray getSerialArray(int index) {
	return (SerialArray) get(index);
    }

    public SerialNumber getSerialNumber(int index) {
	return (SerialNumber) get(index);
    }

    public Number getNumber(int index) {
	return getSerialNumber(index).getAsNumber();
    }

    public byte getByte(int index) {
	return getSerialNumber(index).getAsByte();
    }

    public short getShort(int index) {
	return getSerialNumber(index).getAsShort();
    }

    public int getInt(int index) {
	return getSerialNumber(index).getAsInt();
    }

    public long getLong(int index) {
	return getSerialNumber(index).getAsLong();
    }

    public float getFloat(int index) {
	return getSerialNumber(index).getAsFloat();
    }

    public double getDouble(int index) {
	return getSerialNumber(index).getAsDouble();
    }

    public boolean getBoolean(int index) {
	return ((SerialBoolean) get(index)).getValue();
    }

    public char getChar(int index) {
	return ((SerialChar) get(index)).getValue();
    }

    public String getString(int index) {
	return ((SerialString) get(index)).getValue();
    }

    public void add(SerialNode node) {
	children.add(node);
	if (node != null)
	    node.setParent(this);
    }

    public void addReference(SerialNode ref) {
	add(new SerialReference(ref));
    }

    public void addByte(byte value) {
	add(new SerialByte(value));
    }

    public void addShort(short value) {
	add(new SerialShort(value));
    }

    public void addInt(int value) {
	add(new SerialInt(value));
    }

    public void addLong(long value) {
	add(new SerialLong(value));
    }

    public void addFloat(float value) {
	add(new SerialFloat(value));
    }

    public void addDouble(double value) {
	add(new SerialDouble(value));
    }

    public void addBoolean(boolean value) {
	add(new SerialBoolean(value));
    }

    public void addChar(char value) {
	add(new SerialChar(value));
    }

    public void addString(String value) {
	add(new SerialString(value));
    }

    public void insert(int index, SerialNode node) {
	children.add(index, node);
	node.setParent(this);
    }

    public void insertReference(int index, SerialNode ref) {
	insert(index, new SerialReference(ref));
    }

    public void insertByte(int index, byte value) {
	insert(index, new SerialByte(value));
    }

    public void insertShort(int index, short value) {
	insert(index, new SerialShort(value));
    }

    public void insertInt(int index, int value) {
	insert(index, new SerialInt(value));
    }

    public void insertLong(int index, long value) {
	insert(index, new SerialLong(value));
    }

    public void insertFloat(int index, float value) {
	insert(index, new SerialFloat(value));
    }

    public void insertDouble(int index, double value) {
	insert(index, new SerialDouble(value));
    }

    public void insertBoolean(int index, boolean value) {
	insert(index, new SerialBoolean(value));
    }

    public void insertChar(int index, char value) {
	insert(index, new SerialChar(value));
    }

    public void insertString(int index, String value) {
	insert(index, new SerialString(value));
    }

    public void set(int index, SerialNode node) {
	SerialNode old = children.set(index, node);
	node.setParent(this);
	if (old != null) {
	    old.setParent(null);
	}
    }

    public void setReference(int index, SerialNode ref) {
	set(index, new SerialReference(ref));
    }

    public void setByte(int index, byte value) {
	set(index, new SerialByte(value));
    }

    public void setShort(int index, short value) {
	set(index, new SerialShort(value));
    }

    public void setInt(int index, int value) {
	set(index, new SerialInt(value));
    }

    public void setLong(int index, long value) {
	set(index, new SerialLong(value));
    }

    public void setFloat(int index, float value) {
	set(index, new SerialFloat(value));
    }

    public void setDouble(int index, double value) {
	set(index, new SerialDouble(value));
    }

    public void setBoolean(int index, boolean value) {
	set(index, new SerialBoolean(value));
    }

    public void setChar(int index, char value) {
	set(index, new SerialChar(value));
    }

    public void setString(int index, String value) {
	set(index, new SerialString(value));
    }
    
    public void remove(int index) {
	SerialNode removed = children.remove(index);
	if (removed != null) {
	    removed.setParent(null);
	}
    }

    public int size() {
	return children.size();
    }

    public boolean isEmpty() {
	return children.isEmpty();
    }

    // FIXME: This iterator should evaluate references
    @Override
    public Iterator<SerialNode> iterator() {
	return children.iterator();
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder("[");
	boolean comma = false;
	for (SerialNode node : children) {
	    if (comma) builder.append(", ");
	    else comma = true;

	    builder.append(node);
	}
	builder.append("]");
	return builder.toString();
    }

    @Override
    public Collection<SerialNode> getChildren() {
	return Collections.unmodifiableCollection(children);
    }
}
