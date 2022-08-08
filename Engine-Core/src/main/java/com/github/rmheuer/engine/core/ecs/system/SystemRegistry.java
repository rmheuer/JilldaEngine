package com.github.rmheuer.engine.core.ecs.system;

import com.github.rmheuer.engine.core.util.InstanceMap;

public final class SystemRegistry {
    private static final InstanceMap<GameSystem> systems = new InstanceMap<>();

    public static <T extends GameSystem> T getInstance(Class<T> type) {
        T system = systems.getInstance(type);
        if (system != null)
            return system;

        system = instantiateSystem(type);
        systems.setInstance(system);

        return system;
    }

    private static <T extends GameSystem> T instantiateSystem(Class<T> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate system " + type, e);
        }
    }
}
