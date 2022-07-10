package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialDouble;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class DoubleCodec implements TypeCodec<Double> {
    public static final DoubleCodec INSTANCE = new DoubleCodec();

    private DoubleCodec() {}

    @Override
    public SerialNode serialize(Double d, SerializationContext ctx) {
        return new SerialDouble(d);
    }

    @Override
    public Double deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialDouble) node).getValue();
    }
}
