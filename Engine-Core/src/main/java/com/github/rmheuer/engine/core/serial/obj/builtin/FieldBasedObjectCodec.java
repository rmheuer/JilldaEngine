package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.Transient;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;
import com.github.rmheuer.engine.core.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

// TODO: Detect @SerializeWith on fields
public final class FieldBasedObjectCodec<T> implements TypeCodec<T> {
    private final Class<T> type;
    private final Set<Field> fields;

    public FieldBasedObjectCodec(Class<T> type) {
        this.type = type;
        fields = ReflectUtils.getAllFields(type);
    }

    @Override
    public SerialNode serialize(T t, SerializationContext ctx) {
        if (type.getName().startsWith("java.") || type.getName().startsWith("sun."))
            System.out.println("Warning: Serializing internal class " + type.getName() + ", this is typically not what you want");

        SerialObject obj = new SerialObject();
        for (Field field : fields) {
            String name = field.getName();

            int mods = field.getModifiers();
            boolean trans = Modifier.isTransient(mods);
            boolean stat = Modifier.isStatic(mods);

            if (!trans && !stat && !field.getType().isAnnotationPresent(Transient.class)) {
                Object value;
                try {
                    field.setAccessible(true);
                    value = field.get(t);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to get value of " + field);
                }
                obj.put(name, ctx.serialize(value, field.getType()));
            }
        }
        return obj;
    }

    @Override
    public T deserialize(SerialNode node, DeserializationContext ctx) {
        T instance;
        try {
            instance = type.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate " + type, e);
        }

        SerialObject obj = (SerialObject) node;
        for (Field field : fields) {
            int mods = field.getModifiers();
            boolean trans = Modifier.isTransient(mods);
            boolean stat = Modifier.isStatic(mods);

            if (!trans && !stat && !field.getType().isAnnotationPresent(Transient.class)) {
                SerialNode valueNode = obj.get(field.getName());
                Object value = ctx.deserialize(valueNode, field.getType());

                try {
                    field.setAccessible(true);
                    field.set(instance, value);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to set field: " + field, e);
                }
            }
        }

        return instance;
    }
}
