package com.github.rmheuer.engine.core.serial2.obj;

import com.github.rmheuer.engine.core.serial2.node.SerialNode;

public final class ObjectSerializer {
    public SerialNode serialize(Object obj) { return serialize(obj, obj.getClass()); }
    public SerialNode serialize(Object obj, Class<?> inferredType) {
        return new SerializationCtx().serialize(obj, inferredType);
    }

    public <T> T deserialize(SerialNode serial, Class<T> type) {
        return new DeserializationCtx().deserialize(serial, type);
    }
}
