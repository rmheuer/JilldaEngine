package com.github.rmheuer.engine.core.module;

import com.github.rmheuer.engine.core.main.Game;

import java.util.Collection;
import java.util.Collections;

public interface GameModule {
    // Gets the other modules that this module depends on
    default Collection<Class<? extends GameModule>> getDependencies() {
        return Collections.emptySet();
    }

    // Initializes a world. Typically this would be used to add systems to the world
    default void initializeWorld(Game.WorldBuilder builder) {}

    default void init() {}

    default void preUpdate() {}
    default void postUpdate() {}
    default void preFixedUpdate() {}
    default void postFixedUpdate() {}

    default void close() {}
}
