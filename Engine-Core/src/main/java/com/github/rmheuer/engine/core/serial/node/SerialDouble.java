package com.github.rmheuer.engine.core.serial.node;

public final class SerialDouble extends SerialNumber {
    private double value;

    public SerialDouble(double value) {
	this.value = value;
    }

    public double getValue() {
	return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override public byte   getAsByte()   { return (byte) value; }
    @Override public short  getAsShort()  { return (short) value; }
    @Override public int    getAsInt()    { return (int) value; }
    @Override public long   getAsLong()   { return (long) value; }
    @Override public float  getAsFloat()  { return (float) value; }
    @Override public double getAsDouble() { return value; }
    @Override public Number getAsNumber() { return value; }

    @Override
    public String toString() {
	return value + "D";
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (o == this) return true;
	if (o.getClass().equals(getClass())) return false;

	SerialDouble d = (SerialDouble) o;
	return value == d.value;
    }

    @Override
    public int hashCode() {
	return Double.hashCode(value);
    }
}
