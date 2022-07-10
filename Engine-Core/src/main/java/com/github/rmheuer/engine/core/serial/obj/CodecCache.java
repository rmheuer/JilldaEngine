package com.github.rmheuer.engine.core.serial.obj;

import com.github.rmheuer.engine.core.serial.obj.builtin.ArrayCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.EnumCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.FieldBasedObjectCodec;

import java.util.HashMap;
import java.util.Map;

public final class CodecCache {
    private final Map<Class<?>, TypeCodec<?>> codecCache;

    public CodecCache() {
        codecCache = new HashMap<>();
    }

    public <T> TypeCodec<T> getCodecForType(Class<T> type) {
        if (codecCache.containsKey(type)) {
            @SuppressWarnings("unchecked")
            TypeCodec<T> codec = (TypeCodec<T>) codecCache.get(type);
            return codec;
        }

        TypeCodec<T> codec;

        SerializeWith typeAnnotation = type.getAnnotation(SerializeWith.class);
        if (typeAnnotation != null) {
            Class<?> codecType = typeAnnotation.value();
            if (!TypeCodec.class.isAssignableFrom(codecType)) {
                throw new RuntimeException("Not a TypeCodec: " + codecType);
            }
            try {
                @SuppressWarnings("unchecked")
                TypeCodec<T> customCodec = (TypeCodec<T>) codecType.getConstructor().newInstance();
                codec = customCodec;
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate TypeCodec: " + codecType, e);
            }
        } else if (type.isEnum()) {
            @SuppressWarnings("unchecked")
            TypeCodec<T> enumCodec = (TypeCodec<T>) new EnumCodec(type);
            codec = enumCodec;
        } else if (type.isArray()) {
            @SuppressWarnings("unchecked")
            TypeCodec<T> arrayCodec = (TypeCodec<T>) new ArrayCodec(type);
            codec = arrayCodec;
        } else {
            codec = new FieldBasedObjectCodec<>(type);
        }

        codecCache.put(type, codec);
        return codec;
    }

    public <T> void register(Class<T> type, TypeCodec<T> codec) {
        codecCache.put(type, codec);
    }
}
