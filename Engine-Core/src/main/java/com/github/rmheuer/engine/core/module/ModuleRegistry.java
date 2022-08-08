package com.github.rmheuer.engine.core.module;

import com.github.rmheuer.engine.core.util.InstanceMap;

public final class ModuleRegistry {
    private static final InstanceMap<GameModule> modules = new InstanceMap<>();

    public static <T extends GameModule> T getInstance(Class<T> type) {
        T module = modules.getInstance(type);
        if (module != null)
            return module;

        module = instantiateModule(type);
        modules.setInstance(module);

        return module;
    }

    private static <T extends GameModule> T instantiateModule(Class<T> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate module " + type, e);
        }
    }
}
