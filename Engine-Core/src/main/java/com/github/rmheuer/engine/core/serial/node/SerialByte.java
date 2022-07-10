package com.github.rmheuer.engine.core.serial.node;

public final class SerialByte extends SerialNumber {
    private byte value;

    public SerialByte(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public byte getAsByte() {
        return value;
    }

    @Override
    public short getAsShort() {
        return value;
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public long getAsLong() {
        return value;
    }

    @Override
    public float getAsFloat() {
        return value;
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public Number getAsNumber() {
        return value;
    }

    @Override
    public String toString() {
        return value + "b";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!o.getClass().equals(getClass())) return false;

        SerialByte b = (SerialByte) o;
        return value == b.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
