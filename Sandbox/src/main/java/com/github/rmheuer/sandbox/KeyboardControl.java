package com.github.rmheuer.sandbox;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.math.MathUtils;

public final class KeyboardControl implements Component {
    public float moveSpeed = 2;
    public float turnSpeed = MathUtils.fPI;

    public KeyboardControl() {}

    public KeyboardControl(float moveSpeed, float turnSpeed) {
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
    }
}
