package com.github.rmheuer.engine.core.serial2.node;

public interface NumericNode extends SerialNode {
    byte getByte();
    short getShort();
    int getInt();
    long getLong();
    float getFloat();
    double getDouble();
}
