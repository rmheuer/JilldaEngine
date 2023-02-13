package com.github.rmheuer.engine.core.serial2.obj.type;

import com.github.rmheuer.engine.core.serial2.node.SerialArray;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.obj.DeserializationCtx;
import com.github.rmheuer.engine.core.serial2.obj.SerializationCtx;

import java.lang.reflect.Array;

public final class ArraySerializer implements TypeSerializer {
    public static final ArraySerializer INSTANCE = new ArraySerializer();

    private ArraySerializer() {}

    @Override
    public SerialNode serialize(Object obj, SerializationCtx ctx) {
        Class<?> elementType = obj.getClass().getComponentType();

        SerialArray arr = new SerialArray();
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            arr.add(ctx.serialize(Array.get(obj, i), elementType));
        }

        return arr;
    }

    @Override
    public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
        Class<?> elementType = type.getComponentType();

        SerialArray arr = (SerialArray) node;
        Object array = Array.newInstance(elementType, arr.length());
        for (int i = 0; i < arr.length(); i++) {
            Array.set(array, i, ctx.deserialize(arr.getActual(i), elementType));
        }

        return array;
    }
}
