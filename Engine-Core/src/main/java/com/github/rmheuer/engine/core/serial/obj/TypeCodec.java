package com.github.rmheuer.engine.core.serial.obj;

import com.github.rmheuer.engine.core.serial.node.SerialNode;

public interface TypeCodec<T> {
    SerialNode serialize(T t, SerializationContext ctx);

    T deserialize(SerialNode node, DeserializationContext ctx);
}
