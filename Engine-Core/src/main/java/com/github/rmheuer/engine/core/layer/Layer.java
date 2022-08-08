package com.github.rmheuer.engine.core.layer;

import com.github.rmheuer.engine.core.event.Event;

public interface Layer {
    default void init() {}

    default void update(float delta) {}
    default void fixedUpdate() {}

    default void onEvent(Event event) {}

    default void close() {}
}
