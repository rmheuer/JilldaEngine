package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.math.*;

public final class PerspectiveProjection implements Projection {
    private final Matrix4f matrix;
    private boolean matrixDirty = false;
    private float aspect;
    private float fov;
    private float near;
    private float far;

    private float screenWidth, screenHeight;

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
    public void resize(float width, float height) {
        screenWidth = width;
        screenHeight = height;

        aspect = width / (float) height;
        matrixDirty = true;
    }

    @Override
    public Ray3f pixelToRay(float pixelX, float pixelY) {
        float x = (2.0f * pixelX) / screenWidth - 1.0f;
        float y = 1.0f - (2.0f * pixelY) / screenHeight;
        Vector4f clipSpace = new Vector4f(x, y, -1, 1);
        Vector4f eyeSpace = new Matrix4f(matrix).invert().mul(clipSpace);
        return new Ray3f(new Vector3f(0, 0, 0), new Vector3f(eyeSpace.x, eyeSpace.y, -1).normalize());
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
