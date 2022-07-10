package com.github.rmheuer.engine.core.serial.node;

public final class SerialFloat extends SerialNumber {
    private float value;

    public SerialFloat(float value) {
	this.value = value;
    }

    public float getValue() {
	return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override public byte   getAsByte()   { return (byte) value; }
    @Override public short  getAsShort()  { return (short) value; }
    @Override public int    getAsInt()    { return (int) value; }
    @Override public long   getAsLong()   { return (long) value; }
    @Override public float  getAsFloat()  { return value; }
    @Override public double getAsDouble() { return value; }
    @Override public Number getAsNumber() { return value; }

    @Override
    public String toString() {
	return value + "f";
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (o == this) return true;
	if (!o.getClass().equals(getClass())) return false;

	SerialFloat f = (SerialFloat) o;
	return value == f.value;
    }

    @Override
    public int hashCode() {
	return Float.hashCode(value);
    }
}
