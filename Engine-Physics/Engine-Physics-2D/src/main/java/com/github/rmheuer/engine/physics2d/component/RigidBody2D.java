package com.github.rmheuer.engine.physics2d.component;

import com.github.rmheuer.engine.core.ecs.component.Component;

public final class RigidBody2D implements Component {
    public enum Type {
        STATIC,
        DYNAMIC,
        KINEMATIC
    }

    private Type type;
    private boolean fixedRotation;
    private float gravityScale;

    private Object runtimeBody;

    public RigidBody2D() {
        this(Type.STATIC);
    }

    public RigidBody2D(Type type) {
        this.type = type;
        fixedRotation = false;
        gravityScale = 1.0f;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public boolean hasRuntimeBody() {
        return runtimeBody != null;
    }

    public Object getRuntimeBody() {
        return runtimeBody;
    }

    public void setRuntimeBody(Object runtimeBody) {
        this.runtimeBody = runtimeBody;
    }
}
