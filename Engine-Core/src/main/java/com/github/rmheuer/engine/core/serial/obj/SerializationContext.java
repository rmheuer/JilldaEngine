package com.github.rmheuer.engine.core.serial.obj;

import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.node.SerialReference;

import java.util.IdentityHashMap;
import java.util.Map;

public final class SerializationContext {
    private final CodecCache codecCache;
    private final Map<Object, SerialNode> objectCache;

    private Object userData;

    public SerializationContext(CodecCache codecCache) {
        this.codecCache = codecCache;
        objectCache = new IdentityHashMap<>();
    }

    public SerialNode serialize(Object obj) {
        System.out.println("Serializing " + obj);

        if (obj.getClass().isAnnotationPresent(Transient.class)) {
            return null;
        }

        if (objectCache.containsKey(obj)) {
            return new SerialReference(objectCache.get(obj));
        }

        TypeCodec<?> codec = codecCache.getCodecForType(obj.getClass());

        @SuppressWarnings("unchecked")
        TypeCodec<Object> objCodec = (TypeCodec<Object>) codec;

        SerialNode node = objCodec.serialize(obj, this);
        objectCache.put(obj, node);

        return node;
    }

    /**
     * Use this method to serialize an object that may be multiple
     * different types.
     *
     * @param o object to serialize
     * @param inferredType default type
     * @return serialized node
     */
    public SerialNode serialize(Object o, Class<?> inferredType) {
        System.out.println("Serializing " + o + " (inferred " + inferredType + ")");

        if (o == null)
            return null;

        if (o.getClass().equals(inferredType)) {
            return serialize(o);
        } else {
            SerialObject obj = new SerialObject();
            obj.putString("class", o.getClass().getName());
            obj.put("value", serialize(o));
            return obj;
        }
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
