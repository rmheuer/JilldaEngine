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

    private ReflectUtils() {
        throw new AssertionError();
    }
}
