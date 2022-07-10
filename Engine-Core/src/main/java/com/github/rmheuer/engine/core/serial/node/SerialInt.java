package com.github.rmheuer.engine.core.serial.node;

public final class SerialInt extends SerialNumber {
    private int value;

    public SerialInt(int value) {
	this.value = value;
    }

    public int getValue() {
	return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override public byte   getAsByte()   { return (byte) value; }
    @Override public short  getAsShort()  { return (short) value; }
    @Override public int    getAsInt()    { return value; }
    @Override public long   getAsLong()   { return value; }
    @Override public float  getAsFloat()  { return value; }
    @Override public double getAsDouble() { return value; }
    @Override public Number getAsNumber() { return value; }

    @Override
    public String toString() {
	return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (o == this) return true;
	if (!o.getClass().equals(getClass())) return false;

	SerialInt i = (SerialInt) o;
	return value == i.value;
    }

    @Override
    public int hashCode() {
	return value;
    }
}
