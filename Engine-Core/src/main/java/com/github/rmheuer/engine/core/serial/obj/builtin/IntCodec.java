package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialInt;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class IntCodec implements TypeCodec<Integer> {
    public static final IntCodec INSTANCE = new IntCodec();

    private IntCodec() {}

    @Override
    public SerialNode serialize(Integer i, SerializationContext ctx) {
        return new SerialInt(i);
    }

    @Override
    public Integer deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialInt) node).getValue();
    }
}
