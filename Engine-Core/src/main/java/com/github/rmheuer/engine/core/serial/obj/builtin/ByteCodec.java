package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialByte;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class ByteCodec implements TypeCodec<Byte> {
    public static final ByteCodec INSTANCE = new ByteCodec();

    private ByteCodec() {}

    @Override
    public SerialNode serialize(Byte b, SerializationContext ctx) {
        return new SerialByte(b);
    }

    @Override
    public Byte deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialByte) node).getValue();
    }
}
