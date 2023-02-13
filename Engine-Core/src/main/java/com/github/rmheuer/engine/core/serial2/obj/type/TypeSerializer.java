package com.github.rmheuer.engine.core.serial2.obj.type;

import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.obj.DeserializationCtx;
import com.github.rmheuer.engine.core.serial2.obj.SerializationCtx;

public interface TypeSerializer {
    SerialNode serialize(Object obj, SerializationCtx ctx);
    Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx);
}
