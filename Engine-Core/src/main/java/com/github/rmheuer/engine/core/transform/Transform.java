package com.github.rmheuer.engine.core.transform;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Vector3f;

// The Euler angles for rotation are applied in z, x, y order.
public final class Transform implements Component {
    // Local
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    // Global, set by propagate system
    final Matrix4f global;

    public Transform() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = new Vector3f(1, 1, 1);

        global = new Matrix4f();
    }

    public Matrix4f getMatrix() {
        return new Matrix4f()
                .translate(position)
                .rotateY(rotation.y)
                .rotateX(rotation.x)
                .rotateZ(rotation.z)
                .scale(scale);
    }

    public Matrix4f getGlobalMatrix() {
        return new Matrix4f(global);
    }

    public Matrix4f getInverseMatrix() {
        return getMatrix().invert();
    }

    public Matrix4f getGlobalInverseMatrix() {
        return getGlobalMatrix().invert();
    }

    public Vector3f getForward() {
        return new Vector3f(0, 0, -1).rotateZ(rotation.z).rotateX(rotation.x).rotateY(rotation.y);
    }

    public Vector3f getUp() {
        return new Vector3f(0, 1, 0).rotateZ(rotation.z).rotateX(rotation.x).rotateY(rotation.y);
    }

    public Vector3f getRight() {
        return new Vector3f(1, 0, 0).rotateZ(rotation.z).rotateX(rotation.x).rotateY(rotation.y);
    }

    public Vector3f getGlobalForward() {
        return global.transformDirection(new Vector3f(0, 0, -1));
    }

    public Vector3f getGlobalUp() {
        return global.transformDirection(new Vector3f(0, 1, 0));
    }

    public Vector3f getGlobalRight() {
        return global.transformDirection(new Vector3f(1, 0, 0));
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getGlobalPosition() {
        return new Vector3f(global.m03, global.m13, global.m23);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Transform setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Transform setRotation(Vector3f rotation) {
        this.rotation = rotation;
        return this;
    }

    public Transform setScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }

    public Vector3f transformGlobal(Vector3f vec) {
        return global.transformPosition(vec);
    }
}
