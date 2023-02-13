package com.github.rmheuer.engine.core.serial2.obj.type;

import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.node.SerialObject;
import com.github.rmheuer.engine.core.serial2.obj.DeserializationCtx;
import com.github.rmheuer.engine.core.serial2.obj.SerializationCtx;
import com.github.rmheuer.engine.core.util.ReflectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

// TODO: @SerializeWith on fields
public final class ObjectFieldSerializer implements TypeSerializer {
    private final Map<String, Field> fields;

    public ObjectFieldSerializer(Class<?> type) {
        fields = new HashMap<>();

        for (Field field : ReflectUtils.getAllFields(type)) {
            if (field.isSynthetic() || field.isEnumConstant())
                continue;

            int mods = field.getModifiers();
            if (Modifier.isTransient(mods) || Modifier.isStatic(mods) || Modifier.isFinal(mods))
                continue;

            field.setAccessible(true);
            fields.put(field.getName(), field);
        }
    }

    @Override
    public SerialNode serialize(Object obj, SerializationCtx ctx) {
        SerialObject out = new SerialObject();
        for (Field field : fields.values()) {
            try {
                out.put(field.getName(), ctx.serialize(field.get(obj), field.getType()));
            } catch (ReflectiveOperationException e) {
                System.err.println("Failed to read field: " + field);
                e.printStackTrace();
            }
        }
        return out;
    }

    @Override
    public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
        Object obj;
        try {
            Constructor<?> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            obj = ctor.newInstance();
        } catch (ReflectiveOperationException e) {
            System.err.println("Failed to instantiate object: " + type);
            e.printStackTrace();
            return null;
        }

        SerialObject data = (SerialObject) node;
        for (String key : data.keySet()) {
            Field field = fields.get(key);
            if (field == null)
                continue;

            try {
                field.set(obj, ctx.deserialize(data.getActual(key), field.getType()));
            } catch (ReflectiveOperationException e) {
                System.err.println("Failed to set field: " + field);
                e.printStackTrace();
            }
        }

        return obj;
    }
}
