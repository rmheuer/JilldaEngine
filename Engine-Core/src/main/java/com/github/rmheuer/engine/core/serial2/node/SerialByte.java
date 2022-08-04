package com.github.rmheuer.engine.core.serial2.node;

import java.util.Objects;

public final class SerialByte implements NumericNode {
    private final byte value;

    public SerialByte(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    @Override
    public byte getByte() {
        return value;
    }

    @Override
    public short getShort() {
        return value;
    }

    @Override
    public int getInt() {
        return value;
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
        return value + "b";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialByte that = (SerialByte) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
