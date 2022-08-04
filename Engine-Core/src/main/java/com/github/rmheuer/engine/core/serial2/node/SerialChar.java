package com.github.rmheuer.engine.core.serial2.node;

import com.github.rmheuer.engine.core.util.StringUtils;

import java.util.Objects;

public final class SerialChar {
    private final char value;

    public SerialChar(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return StringUtils.quoteChar(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialChar that = (SerialChar) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
