package com.github.rmheuer.engine.core.serial.node;

public abstract class SerialNumber extends SerialNode {
    public abstract byte getAsByte();
    
    public abstract short getAsShort();

    public abstract int getAsInt();

    public abstract long getAsLong();

    public abstract float getAsFloat();

    public abstract double getAsDouble();

    public abstract Number getAsNumber();
}
