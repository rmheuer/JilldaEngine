package com.github.rmheuer.engine.core.serial.obj;

import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;

public final class DeserializationContext {
    private final CodecCache codecCache;

    private Object userData;

    public DeserializationContext(CodecCache codecCache) {
        this.codecCache = codecCache;
    }

    private <T> T deserializeContent(SerialNode node, Class<T> type) {
        if (node == null)
            return null;

        TypeCodec<T> codec = codecCache.getCodecForType(type);
        return codec.deserialize(node, this);
    }

    public <T> T deserialize(SerialNode node, Class<T> type) {
        Class<? extends T> targetClass = type;
        SerialNode contentNode = node;
        if (node instanceof SerialObject) {
            SerialObject obj = (SerialObject) node;
            if (obj.containsKey("class")) {
                Class<?> specifiedType;
                try {
                    specifiedType = Class.forName(obj.getString("class"));
                } catch (Exception e) {
                    throw new RuntimeException("Could not find specified class", e);
                }

                if (!type.isAssignableFrom(specifiedType)) {
                    throw new RuntimeException("Invalid specified type: " + specifiedType);
                }

                @SuppressWarnings("unchecked")
                Class<? extends T> cast = (Class<? extends T>) specifiedType;
                targetClass = cast;

                contentNode = obj.get("value");
            }
        }

        return deserializeContent(contentNode, targetClass);
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
