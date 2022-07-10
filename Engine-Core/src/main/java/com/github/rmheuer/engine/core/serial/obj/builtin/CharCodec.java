package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialChar;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

public final class CharCodec implements TypeCodec<Character> {
    public static final CharCodec INSTANCE = new CharCodec();

    private CharCodec() {}

    @Override
    public SerialNode serialize(Character c, SerializationContext ctx) {
        return new SerialChar(c);
    }

    @Override
    public Character deserialize(SerialNode node, DeserializationContext ctx) {
        return ((SerialChar) node).getValue();
    }
}
