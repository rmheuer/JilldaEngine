package com.github.rmheuer.engine.core.math;

import com.github.rmheuer.engine.core.ecs.component.Component;

// The Euler angles for rotation are applied in z, x, y order.
public final class Transform implements Component {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform() {
	position = new Vector3f(0, 0, 0);
	rotation = new Vector3f(0, 0, 0);
	scale = new Vector3f(1, 1, 1);
    }

    public Matrix4f getMatrix() {
	return new Matrix4f()
	    .translate(position)
	    .rotateZ(rotation.z)
	    .rotateX(rotation.x)
	    .rotateY(rotation.y)
	    .scale(scale);
    }

    public Matrix4f getInverseMatrix() {
	return getMatrix().invert();
    }

    public Vector3f getForward() {
	return new Vector3f(0, 0, -1).rotateZ(rotation.z).rotateX(rotation.x).rotateY(rotation.y);
    }

    public Vector3f getUp() {
	return new Vector3f(0, 1, 0).rotateZ(rotation.z).rotateX(rotation.x).rotateY(rotation.y);
    }

    public Vector3f getPosition() {
	return position;
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

    public Vector3f transform(Vector3f vec) {
        return getMatrix().transformPosition(vec);
    }
}
