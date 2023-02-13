package com.github.rmheuer.engine.core.serial2.obj;

import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.node.SerialNull;
import com.github.rmheuer.engine.core.serial2.node.SerialObject;
import com.github.rmheuer.engine.core.serial2.obj.type.ArraySerializer;
import com.github.rmheuer.engine.core.serial2.obj.type.CollectionSerializer;
import com.github.rmheuer.engine.core.serial2.obj.type.EnumSerializer;
import com.github.rmheuer.engine.core.serial2.obj.type.MapSerializer;
import com.github.rmheuer.engine.core.serial2.obj.type.ObjectFieldSerializer;
import com.github.rmheuer.engine.core.serial2.obj.type.PrimitiveSerializers;
import com.github.rmheuer.engine.core.serial2.obj.type.TypeSerializer;
import com.github.rmheuer.engine.core.util.Pair;
import com.github.rmheuer.engine.core.util.ReflectUtils;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class SerializationCtx {
    // TODO: Move to ObjectSerializer
    private final Map<Class<?>, SerialMapper<?, ?>> mapperCache;

    public SerializationCtx() {
        mapperCache = new HashMap<>();
    }

    private Pair<TypeSerializer, Boolean> getSerializer(Class<?> type) {
        if (type.isEnum())
            return Pair.of(EnumSerializer.INSTANCE, false);
        if (type.isArray())
            return Pair.of(ArraySerializer.INSTANCE, true);
        if (Collection.class.isAssignableFrom(type))
            return Pair.of(CollectionSerializer.INSTANCE, true);
        if (Map.class.isAssignableFrom(type))
            return Pair.of(MapSerializer.INSTANCE, true);

        TypeSerializer primitive = PrimitiveSerializers.get(type);
        if (primitive != null)
            return Pair.of(primitive, false);

        return Pair.of(new ObjectFieldSerializer(type), true);
    }

    @SuppressWarnings("rawtypes")
    private SerialMapper getMapper(Class<?> type) {
        SerialMapper<?, ?> mapper = mapperCache.get(type);
        if (mapper == null) {
            SerializeWith with = type.getAnnotation(SerializeWith.class);
            if (with == null)
                throw new IllegalArgumentException("Type is not serializable: " + type);
            Class<? extends SerialMapper<?, ?>> mapperType = with.value();
            try {
                Constructor<? extends SerialMapper<?, ?>> ctor = mapperType.getConstructor();
                ctor.setAccessible(true);
                mapper = ctor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to instantiate serial mapper: " + mapperType, e);
            }
            mapperCache.put(type, mapper);
        }
        return mapper;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public SerialNode serialize(Object obj, Class<?> inferredType) {
        if (obj == null)
            return new SerialNull();

        Class<?> actualType = obj.getClass();
        Pair<TypeSerializer, Boolean> pair = getSerializer(actualType);
        TypeSerializer serializer = pair.getA();
        boolean canBeReference = pair.getB();

        if (serializer instanceof ObjectFieldSerializer && !(obj instanceof AutoSerializable)) {
            SerialMapper mapper = getMapper(obj.getClass());
            return serialize(mapper.toSerializable(obj), mapper.getSerialType());
        }

        // TODO: References

        SerialNode serialized = serializer.serialize(obj, this);
        if (!actualType.equals(ReflectUtils.wrap(inferredType))) {
            SerialObject typeInfo = new SerialObject();
            typeInfo.putString("class", actualType.getName());
            typeInfo.put("value", serialized);
            serialized = typeInfo;
        }

        return serialized;
    }
}
