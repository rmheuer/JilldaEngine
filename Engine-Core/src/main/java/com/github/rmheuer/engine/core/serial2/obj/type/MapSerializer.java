package com.github.rmheuer.engine.core.serial2.obj.type;

import com.github.rmheuer.engine.core.serial2.node.SerialArray;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.node.SerialObject;
import com.github.rmheuer.engine.core.serial2.obj.DeserializationCtx;
import com.github.rmheuer.engine.core.serial2.obj.SerializationCtx;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public final class MapSerializer implements TypeSerializer {
    public static final MapSerializer INSTANCE = new MapSerializer();

    private MapSerializer() {}

    @Override
    public SerialNode serialize(Object obj, SerializationCtx ctx) {
        Map<?, ?> map = (Map<?, ?>) obj;
        SerialArray entryArr = new SerialArray();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            SerialObject entryObj = new SerialObject();
            // TODO: Read type parameters
            entryObj.put("key", ctx.serialize(entry.getKey(), Object.class));
            entryObj.put("val", ctx.serialize(entry.getValue(), Object.class));
            entryArr.add(entryObj);
        }
        return entryArr;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
        Class<? extends Map<?, ?>> mapType = (Class<? extends Map<?, ?>>) type;

        Map map;
        try {
            Constructor<? extends Map<?, ?>> ctor = mapType.getConstructor();
            ctor.setAccessible(true);
            map = ctor.newInstance();
        } catch (ReflectiveOperationException e) {
            map = new HashMap<>();
        }

        SerialArray arr = (SerialArray) node;
        for (SerialNode elem : arr) {
            SerialObject obj = (SerialObject) elem;

            Object key = ctx.deserialize(obj.getActual("key"), Object.class);
            Object val = ctx.deserialize(obj.getActual("val"), Object.class);

            map.put(key, val);
        }

        return map;
    }
}
