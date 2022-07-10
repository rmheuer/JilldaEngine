package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialArray;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

import java.lang.reflect.Array;

@SuppressWarnings("rawtypes")
public final class ArrayCodec implements TypeCodec {
    private final Class<?> type;

    public ArrayCodec(Class<?> type) {
        this.type = type;
    }

    @Override
    public SerialNode serialize(Object o, SerializationContext ctx) {
        SerialArray arr = new SerialArray();
        int len = Array.getLength(o);
        for (int i = 0; i < len; i++) {
            Object value = Array.get(o, i);
            arr.add(ctx.serialize(value));
        }
        return arr;
    }

    @Override
    public Object deserialize(SerialNode node, DeserializationContext ctx) {
        SerialArray arr = (SerialArray) node;
        int size = arr.size();

        Class<?> elemType = type.getComponentType();
        Object out = Array.newInstance(elemType, size);

        for (int i = 0; i < size; i++) {
	    System.out.println("Adding " + arr.get(i) + " -> " + ctx.deserialize(arr.get(i), elemType));
            Array.set(out, i, ctx.deserialize(arr.get(i), elemType));
        }

        return out;
    }
}
