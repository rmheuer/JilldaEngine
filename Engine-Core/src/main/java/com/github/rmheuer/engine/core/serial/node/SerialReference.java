package com.github.rmheuer.engine.core.serial.node;

public final class SerialReference extends SerialNode {
    private SerialNode reference;

    public SerialReference(SerialNode reference) {
	this.reference = reference;
    }

    public SerialNode getReference() {
	return reference;
    }

    public void setReference(SerialNode reference) {
	this.reference = reference;
    }

    public SerialNode evaluate() {
	if (reference instanceof SerialReference) {
	    return ((SerialReference) reference).evaluate();
	} else {
	    return reference;
	}
    }

    @Override
    public String toString() {
	return "<ref " + reference.hashCode() + ">";
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (o == this) return true;
	if (!o.getClass().equals(getClass())) return false;

	SerialReference r = (SerialReference) o;
	return reference.equals(r.reference);
    }

    @Override
    public int hashCode() {
	return reference.hashCode();
    }
}
