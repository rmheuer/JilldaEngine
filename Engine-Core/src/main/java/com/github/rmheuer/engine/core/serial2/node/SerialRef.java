package com.github.rmheuer.engine.core.serial2.node;

public final class SerialRef extends SerialNode {
    private SerialNode target;

    public SerialRef() {}

    public SerialRef(SerialNode target) {
        checkTarget(target);
        this.target = target;
    }

    private void checkTarget(SerialNode target) {
        if (target == this)
            throw new IllegalArgumentException("Reference cannot refer to itself");
    }

    public SerialNode eval() {
        if (target == null)
            return new SerialNull();

        if (target instanceof SerialRef)
            return ((SerialRef) target).eval();

        return target;
    }

    public SerialNode getTarget() {
        return target;
    }

    public void setTarget(SerialNode target) {
        checkTarget(target);
        this.target = target;
    }

    @Override
    public String toString() {
        return "<ref " + getRelativePathTo(target) + ">";
    }
}
