package com.github.rmheuer.engine.core.serial2.obj.type;

import com.github.rmheuer.engine.core.serial2.node.SerialArray;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.obj.DeserializationCtx;
import com.github.rmheuer.engine.core.serial2.obj.SerializationCtx;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CollectionSerializer implements TypeSerializer {
    public static final CollectionSerializer INSTANCE = new CollectionSerializer();

    private CollectionSerializer() {}

    @Override
    public SerialNode serialize(Object obj, SerializationCtx ctx) {
        SerialArray arr = new SerialArray();
        Collection<?> collection = (Collection<?>) obj;
        for (Object elem : collection) {
            // TODO: Read type parameter
            arr.add(ctx.serialize(elem, Object.class));
        }
        return arr;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
        Class<Collection<?>> collType = (Class<Collection<?>>) type;

        Collection collection;
        try {
            Constructor<Collection<?>> ctor = collType.getConstructor();
            ctor.setAccessible(true);
            collection = ctor.newInstance();
        } catch (ReflectiveOperationException e) {
            // Fall back to common collection implementations
            if (List.class.isAssignableFrom(collType))
                collection = new ArrayList<>();
            else if (Set.class.isAssignableFrom(collType))
                collection = new HashSet<>();
            else {
                System.err.println("Failed to find collection constructor for " + collType.getName());
                return null;
            }
        }

        SerialArray arr = (SerialArray) node;
        int size = arr.length();
        for (int i = 0; i < size; i++) {
            SerialNode elem = arr.getActual(i);
            collection.add(ctx.deserialize(elem, Object.class));
        }

        return collection;
    }
}
