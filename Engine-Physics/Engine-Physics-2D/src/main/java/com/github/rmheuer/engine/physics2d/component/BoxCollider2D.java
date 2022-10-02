package com.github.rmheuer.engine.physics2d.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.math.Vector2f;

public final class BoxCollider2D implements Component {
    private final Vector2f offset;
    private final Vector2f size;

    private float density;
    private float friction;
    private float restitution;

    public BoxCollider2D() {
        this(new Vector2f(1, 1));
    }

    public BoxCollider2D(Vector2f size) {
        offset = new Vector2f(0, 0);
        this.size = size;

        density = 1;
        friction = 0.5f;
        restitution = 0.0f;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
    }

    public void setSize(Vector2f size) {
        this.size.set(size);
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }
}
