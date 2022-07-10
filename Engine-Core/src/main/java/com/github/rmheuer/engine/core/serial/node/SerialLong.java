package com.github.rmheuer.engine.core.serial.node;

public final class SerialLong extends SerialNumber {
    private long value;

    public SerialLong(long value) {
	this.value = value;
    }

    public long getValue() {
	return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override public byte   getAsByte()   { return (byte) value; }
    @Override public short  getAsShort()  { return (short) value; }
    @Override public int    getAsInt()    { return (int) value; }
    @Override public long   getAsLong()   { return value; }
    @Override public float  getAsFloat()  { return value; }
    @Override public double getAsDouble() { return value; }
    @Override public Number getAsNumber() { return value; }

    @Override
    public String toString() {
	return value + "L";
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (o == this) return true;
	if (!o.getClass().equals(getClass())) return false;

	SerialLong l = (SerialLong) o;
	return value == l.value;
    }

    @Override
    public int hashCode() {
	return Long.hashCode(value);
    }
}
