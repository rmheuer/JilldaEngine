package com.github.rmheuer.engine.core.serial2.obj.type;

import com.github.rmheuer.engine.core.serial2.node.NumericNode;
import com.github.rmheuer.engine.core.serial2.node.SerialBoolean;
import com.github.rmheuer.engine.core.serial2.node.SerialByte;
import com.github.rmheuer.engine.core.serial2.node.SerialChar;
import com.github.rmheuer.engine.core.serial2.node.SerialDouble;
import com.github.rmheuer.engine.core.serial2.node.SerialFloat;
import com.github.rmheuer.engine.core.serial2.node.SerialInt;
import com.github.rmheuer.engine.core.serial2.node.SerialLong;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.node.SerialShort;
import com.github.rmheuer.engine.core.serial2.node.SerialString;
import com.github.rmheuer.engine.core.serial2.node.TextualNode;
import com.github.rmheuer.engine.core.serial2.obj.DeserializationCtx;
import com.github.rmheuer.engine.core.serial2.obj.SerializationCtx;

import java.util.HashMap;
import java.util.Map;

public enum PrimitiveSerializers implements TypeSerializer {
    BOOLEAN {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialBoolean((boolean) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((SerialBoolean) node).getValue();
        }
    },
    BYTE {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialByte((byte) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((NumericNode) node).getByte();
        }
    },
    SHORT {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialShort((short) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((NumericNode) node).getShort();
        }
    },
    INT {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialInt((int) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((NumericNode) node).getInt();
        }
    },
    LONG {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialLong((long) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((NumericNode) node).getLong();
        }
    },
    FLOAT {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialFloat((float) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((NumericNode) node).getFloat();
        }
    },
    DOUBLE {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialDouble((double) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((NumericNode) node).getDouble();
        }
    },
    CHAR {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialChar((char) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((TextualNode) node).getChar();
        }
    },
    STRING {
        @Override
        public SerialNode serialize(Object obj, SerializationCtx ctx) {
            return new SerialString((String) obj);
        }

        @Override
        public Object deserialize(SerialNode node, Class<?> type, DeserializationCtx ctx) {
            return ((TextualNode) node).getString();
        }
    };

    private static final Map<Class<?>, PrimitiveSerializers> TYPE_MAP = new HashMap<>();
    static {
        TYPE_MAP.put(boolean.class, BOOLEAN);
        TYPE_MAP.put(byte.class, BYTE);
        TYPE_MAP.put(short.class, SHORT);
        TYPE_MAP.put(int.class, INT);
        TYPE_MAP.put(long.class, LONG);
        TYPE_MAP.put(float.class, FLOAT);
        TYPE_MAP.put(double.class, DOUBLE);
        TYPE_MAP.put(char.class, CHAR);

        TYPE_MAP.put(Boolean.class, BOOLEAN);
        TYPE_MAP.put(Byte.class, BYTE);
        TYPE_MAP.put(Short.class, SHORT);
        TYPE_MAP.put(Integer.class, INT);
        TYPE_MAP.put(Long.class, LONG);
        TYPE_MAP.put(Float.class, FLOAT);
        TYPE_MAP.put(Double.class, DOUBLE);
        TYPE_MAP.put(Character.class, CHAR);
        TYPE_MAP.put(String.class, STRING);
    }

    public static PrimitiveSerializers get(Class<?> type) {
        return TYPE_MAP.get(type);
    }
}
