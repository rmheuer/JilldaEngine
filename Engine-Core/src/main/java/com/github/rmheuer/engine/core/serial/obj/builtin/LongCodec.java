package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialLong;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class LongCodec implements TypeCodec<Long> {
    public static final LongCodec INSTANCE = new LongCodec();

    private LongCodec() {}

    @Override
    public SerialNode serialize(Long l, SerializationContext ctx) {
        return new SerialLong(l);
    }

    @Override
    public Long deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialLong) node).getValue();
    }
}
