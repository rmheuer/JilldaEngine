package com.github.rmheuer.engine.core.serial2.node;

import java.util.Objects;

public final class SerialLong implements NumericNode {
    private final long value;

    public SerialLong(long value) {
        this.value = value;
    }

    public long getValue() {
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
        return value;
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
    public String toString() {
        return value + "L";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialLong that = (SerialLong) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
