package com.github.rmheuer.engine.core.serial.node;

import com.github.rmheuer.engine.core.util.StringUtils;

public final class SerialChar extends SerialNode {
    private char value;

    public SerialChar(char value) {
	this.value = value;
    }

    public char getValue() {
	return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    @Override
    public String toString() {
	return StringUtils.quoteChar(value);
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (o == this) return true;
	if (!o.getClass().equals(getClass())) return false;

	SerialChar c = (SerialChar) o;
	return value == c.value;
    }

    @Override
    public int hashCode() {
	return value;
    }
}
