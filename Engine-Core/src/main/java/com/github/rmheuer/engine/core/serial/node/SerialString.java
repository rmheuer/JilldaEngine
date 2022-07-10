package com.github.rmheuer.engine.core.serial.node;

import com.github.rmheuer.engine.core.util.StringUtils;

public final class SerialString extends SerialNode {
    private String value;

    public SerialString(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
	return StringUtils.quoteString(value);
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (o == this) return true;
	if (!o.getClass().equals(getClass())) return false;

	SerialString c = (SerialString) o;
	return value.equals(c.value);
    }

    @Override
    public int hashCode() {
	return value.hashCode();
    }
}
