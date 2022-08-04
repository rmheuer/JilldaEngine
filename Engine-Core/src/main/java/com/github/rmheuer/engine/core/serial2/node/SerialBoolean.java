package com.github.rmheuer.engine.core.serial2.node;

import java.util.Objects;

public final class SerialBoolean {
    public static final SerialBoolean TRUE = new SerialBoolean(true);
    public static final SerialBoolean FALSE = new SerialBoolean(false);

    public static SerialBoolean valueOf(boolean val) {
        return val ? TRUE : FALSE;
    }

    private final boolean value;

    public SerialBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialBoolean that = (SerialBoolean) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
