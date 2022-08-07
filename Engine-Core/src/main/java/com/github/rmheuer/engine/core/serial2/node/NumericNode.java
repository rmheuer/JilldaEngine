package com.github.rmheuer.engine.core.serial2.node;

public abstract class NumericNode extends SerialNode {
    public abstract byte getByte();
    public abstract short getShort();
    public abstract int getInt();
    public abstract long getLong();
    public abstract float getFloat();
    public abstract double getDouble();

    public abstract boolean isWhole();
}
