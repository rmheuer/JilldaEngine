package com.github.rmheuer.engine.core.serial2.node;

import java.util.Objects;

public final class SerialFloat extends NumericNode {
    private final float value;

    public SerialFloat(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public byte getByte() {
        return (byte) value;
    }

    @Override
    public short getShort() {
        return (short) value;
    }

    @Override
    public int getInt() {
        return (int) value;
    }

    @Override
    public long getLong() {
        return (long) value;
    }

    @Override
    public float getFloat() {
        return value;
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public boolean isWhole() {
        return false;
    }

    @Override
    public String toString() {
        return value + "f";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialFloat that = (SerialFloat) o;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
