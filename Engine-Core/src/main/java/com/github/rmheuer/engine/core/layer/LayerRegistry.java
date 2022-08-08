package com.github.rmheuer.engine.core.layer;

import com.github.rmheuer.engine.core.util.InstanceMap;

public final class LayerRegistry {
    private static final InstanceMap<Layer> layers = new InstanceMap<>();

    public static <T extends Layer> T getInstance(Class<T> type) {
        T layer = layers.getInstance(type);
        if (layer != null)
            return layer;

        layer = instantiateLayer(type);
        layers.setInstance(layer);

        return layer;
    }

    private static <T extends Layer> T instantiateLayer(Class<T> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate layer " + type, e);
        }
    }
}
