package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialString;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class StringCodec implements TypeCodec<String> {
    public static final StringCodec INSTANCE = new StringCodec();

    private StringCodec() {}

    @Override
    public SerialNode serialize(String s, SerializationContext ctx) {
        return new SerialString(s);
    }

    @Override
    public String deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialString) node).getValue();
    }
}
