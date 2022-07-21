package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Matrix4f;

public final class PerspectiveProjection implements Projection {
    private final Matrix4f matrix;
    private boolean matrixDirty = false;
    private float aspect;
    private float fov;
    private float near;
    private float far;

    public PerspectiveProjection() {
        this(MathUtils.fPI / 2, 0.1f, 1000f);
    }

    public PerspectiveProjection(float fov, float near, float far) {
        matrix = new Matrix4f();
        this.fov = fov;
        this.near = near;
        this.far = far;
    }

    private void recreateMatrix() {
        matrix.perspective(fov, aspect, near, far);
    }

    @Override
    public Matrix4f getMatrix() {
        if (matrixDirty) {
            recreateMatrix();
            matrixDirty = false;
        }

        return matrix;
    }

    @Override
    public void resize(int width, int height) {
        aspect = width / (float) height;
        matrixDirty = true;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
        matrixDirty = true;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
        matrixDirty = true;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
        matrixDirty = true;
    }
}
