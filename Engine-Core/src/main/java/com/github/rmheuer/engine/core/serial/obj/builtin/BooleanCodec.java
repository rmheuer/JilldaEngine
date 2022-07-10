package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialBoolean;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class BooleanCodec implements TypeCodec<Boolean> {
    public static final BooleanCodec INSTANCE = new BooleanCodec();

    private BooleanCodec() {}

    @Override
    public SerialNode serialize(Boolean b, SerializationContext ctx) {
        return new SerialBoolean(b);
    }

    @Override
    public Boolean deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialBoolean) node).getValue();
    }
}
