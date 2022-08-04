package com.github.rmheuer.engine.core.serial2.node;

import com.github.rmheuer.engine.core.util.StringUtils;

import java.util.Objects;

public final class SerialString {
    private final String value;

    public SerialString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return StringUtils.quoteString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialString that = (SerialString) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
