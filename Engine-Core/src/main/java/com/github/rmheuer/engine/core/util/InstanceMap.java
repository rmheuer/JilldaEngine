package com.github.rmheuer.engine.core.util;

import java.util.HashMap;
import java.util.Map;

public final class InstanceMap<T> {
    private final Map<Class<?>, T> instances;

    public InstanceMap() {
        instances = new HashMap<>();
    }

    public void setInstance(T t) {
        instances.put(t.getClass(), t);
    }

    public <S extends T> S getInstance(Class<S> type) {
        T value = instances.get(type);

        @SuppressWarnings("unchecked")
        S cast = (S) value;

        return cast;
    }

    public void removeInstance(Class<? extends T> type) {
        instances.remove(type);
    }
}
