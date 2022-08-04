package com.github.rmheuer.engine.core.serial2.node;

import java.util.Objects;

public final class SerialInt implements NumericNode {
    private final int value;

    public SerialInt(int value) {
        this.value = value;
    }

    public int getValue() {
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
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialInt serialInt = (SerialInt) o;
        return value == serialInt.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
