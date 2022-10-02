package com.github.rmheuer.engine.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class LazyCache<K, V> {
    private final Map<K, V> map;
    private final Function<K, V> mappingFn;

    public LazyCache(Function<K, V> mappingFn) {
        map = new HashMap<>();
        this.mappingFn = mappingFn;
    }

    public boolean isCached(K k) {
        return map.containsKey(k);
    }

    public V get(K k) {
        V v = map.get(k);
        if (v != null)
            return v;

        v = mappingFn.apply(k);
        map.put(k, v);

        return v;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
