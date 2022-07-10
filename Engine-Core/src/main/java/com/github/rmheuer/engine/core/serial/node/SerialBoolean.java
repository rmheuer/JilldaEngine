package com.github.rmheuer.engine.core.serial.node;

public final class SerialBoolean extends SerialNode {
    private boolean value;

    public SerialBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!o.getClass().equals(getClass())) return false;

        SerialBoolean b = (SerialBoolean) o;
        return value == b.value;
    }

    @Override
    public int hashCode() {
        return value ? 1 : 0;
    }
}
