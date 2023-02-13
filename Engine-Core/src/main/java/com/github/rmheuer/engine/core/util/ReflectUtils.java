package com.github.rmheuer.engine.core.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ReflectUtils {
    public static Set<Field> getAllFields(Class<?> type) {
        Set<Field> fields = new HashSet<>();

        while (type.getSuperclass() != null) {
            fields.addAll(Arrays.asList(type.getDeclaredFields()));
            type = type.getSuperclass();
        }

        return fields;
    }

    public static Class<?> wrap(Class<?> type) {
        if (!type.isPrimitive())
            return type;

        if (type.equals(boolean.class)) return Boolean.class;
        if (type.equals(byte.class)) return Byte.class;
        if (type.equals(short.class)) return Short.class;
        if (type.equals(int.class)) return Integer.class;
        if (type.equals(long.class)) return Long.class;
        if (type.equals(float.class)) return Float.class;
        if (type.equals(double.class)) return Double.class;
        if (type.equals(char.class)) return Character.class;

        throw new AssertionError();
    }

    private ReflectUtils() {
        throw new AssertionError();
    }
}
