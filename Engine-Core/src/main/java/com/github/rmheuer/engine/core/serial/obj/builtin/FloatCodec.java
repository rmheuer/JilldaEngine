package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialFloat;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class FloatCodec implements TypeCodec<Float> {
    public static final FloatCodec INSTANCE = new FloatCodec();

    private FloatCodec() {}

    @Override
    public SerialNode serialize(Float f, SerializationContext ctx) {
        return new SerialFloat(f);
    }

    @Override
    public Float deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialFloat) node).getValue();
    }
}
