package com.github.rmheuer.engine.core.serial.node;

public final class SerialShort extends SerialNumber {
    private short value;

    public SerialShort(short value) {
	this.value = value;
    }

    public short getValue() {
	return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Override public byte   getAsByte()   { return (byte) value; }
    @Override public short  getAsShort()  { return value; }
    @Override public int    getAsInt()    { return value; }
    @Override public long   getAsLong()   { return value; }
    @Override public float  getAsFloat()  { return value; }
    @Override public double getAsDouble() { return value; }
    @Override public Number getAsNumber() { return value; }

    @Override
    public String toString() {
	return value + "s";
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (o == this) return true;
	if (!o.getClass().equals(getClass())) return false;

	SerialShort s = (SerialShort) o;
	return value == s.value;
    }

    @Override
    public int hashCode() {
	return value;
    }
}
