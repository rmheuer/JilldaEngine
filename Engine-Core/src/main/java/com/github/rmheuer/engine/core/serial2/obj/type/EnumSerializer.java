package com.github.rmheuer.engine.core.serial2.obj.type;

import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.node.SerialString;
import com.github.rmheuer.engine.core.serial2.node.TextualNode;
import com.github.rmheuer.engine.core.serial2.obj.DeserializationCtx;
import com.github.rmheuer.engine.core.serial2.obj.SerializationCtx;

public final class EnumSerializer implements TypeSerializer {
    public static final EnumSerializer INSTANCE = new EnumSerializer();

    private EnumSerializer() {}

    @Override
    public SerialNode serialize(Object obj, SerializationCtx ctx) {
        String name = ((Enum<?>) obj).name();
        return new SerialString(name);
    }

    private <T extends Enum<T>> T valueOf(String name, Class<?> type) {
        @SuppressWarnings("unchecked")
        Class<T> cast = (Class<T>) type;
        return Enum.valueOf(cast, name);
    }

    @Override
    public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
        return valueOf(((TextualNode) node).getString(), type);
    }
}
