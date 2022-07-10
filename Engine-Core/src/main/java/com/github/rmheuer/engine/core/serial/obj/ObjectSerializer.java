package com.github.rmheuer.engine.core.serial.obj;

import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.obj.builtin.BooleanCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.ByteCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.CharCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.DoubleCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.FloatCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.IntCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.LongCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.ShortCodec;
import com.github.rmheuer.engine.core.serial.obj.builtin.StringCodec;

public final class ObjectSerializer {
    private static final ObjectSerializer INSTANCE = new ObjectSerializer();

    private final CodecCache codecCache;

    private ObjectSerializer() {
        codecCache = new CodecCache();

        // Register built-in codecs
        register(byte.class, ByteCodec.INSTANCE);
        register(short.class, ShortCodec.INSTANCE);
        register(int.class, IntCodec.INSTANCE);
        register(long.class, LongCodec.INSTANCE);
        register(float.class, FloatCodec.INSTANCE);
        register(double.class, DoubleCodec.INSTANCE);
        register(boolean.class, BooleanCodec.INSTANCE);
        register(char.class, CharCodec.INSTANCE);

        register(Byte.class, ByteCodec.INSTANCE);
        register(Short.class, ShortCodec.INSTANCE);
        register(Integer.class, IntCodec.INSTANCE);
        register(Long.class, LongCodec.INSTANCE);
        register(Float.class, FloatCodec.INSTANCE);
        register(Double.class, DoubleCodec.INSTANCE);
        register(Boolean.class, BooleanCodec.INSTANCE);
        register(Character.class, CharCodec.INSTANCE);

        register(String.class, StringCodec.INSTANCE);
    }

    public static ObjectSerializer get() {
        return INSTANCE;
    }

    public SerialNode serialize(Object obj) {
        return new SerializationContext(codecCache).serialize(obj);
    }

    public <T> T deserialize(SerialNode node, Class<T> type) {
        return new DeserializationContext(codecCache).deserialize(node, type);
    }

    public <T> void register(Class<T> type, TypeCodec<T> codec) {
        codecCache.register(type, codec);
    }
}
