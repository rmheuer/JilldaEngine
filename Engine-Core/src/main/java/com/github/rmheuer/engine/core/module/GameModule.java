package com.github.rmheuer.engine.core.module;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;

import java.util.Collection;

public interface GameModule {
    Collection<Class<? extends GameSystem>> getSystems();

    default void init() {}

    default void preUpdate() {}
    default void postUpdate() {}
    default void preFixedUpdate() {}
    default void postFixedUpdate() {}

    default void close() {}
}
