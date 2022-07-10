package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialString;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class EnumCodec implements TypeCodec {
    private final Class type;

    public EnumCodec(Class type) {
        this.type = type;
    }

    @Override
    public SerialNode serialize(Object o, SerializationContext ctx) {
        return new SerialString(o.toString());
    }

    @Override
    public Object deserialize(SerialNode node, DeserializationContext ctx) {
        return Enum.valueOf(type, ((SerialString) node).getValue());
    }
}
