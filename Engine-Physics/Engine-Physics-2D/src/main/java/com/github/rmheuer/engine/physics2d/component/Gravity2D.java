package com.github.rmheuer.engine.physics2d.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.math.Vector2f;

public final class Gravity2D implements Component {
    private final Vector2f acceleration;

    public Gravity2D() {
        acceleration = new Vector2f();
    }

    public Gravity2D(Vector2f acceleration) {
        this.acceleration = new Vector2f(acceleration);
    }

    public Vector2f getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2f acc) {
        acceleration.set(acc);
    }
}
