package com.github.rmheuer.engine.core.ecs.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.event.EventDispatcher;

public interface GameSystem extends SystemNode {
    default void init(World world) {}

    default void update(World world, float delta) {}

    default void fixedUpdate(World world) {}

    default void close(World world) {}
}
