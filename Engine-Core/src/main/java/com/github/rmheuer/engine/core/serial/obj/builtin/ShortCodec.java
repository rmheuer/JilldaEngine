package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialShort;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class ShortCodec implements TypeCodec<Short> {
    public static final ShortCodec INSTANCE = new ShortCodec();

    private ShortCodec() {}

    @Override
    public SerialNode serialize(Short s, SerializationContext ctx) {
        return new SerialShort(s);
    }

    @Override
    public Short deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialShort) node).getValue();
    }
}
