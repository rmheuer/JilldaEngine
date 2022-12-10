package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Ray3f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.transform.Transform;

public final class PrimaryCamera {
    private final Camera camera;
    private final Transform transform;

    public PrimaryCamera(Camera camera, Transform transform) {
        this.camera = camera;
        this.transform = transform;
    }

    public Ray3f getRayAtScreenPos(float x, float y) {
        Ray3f cameraLocalRay = camera.getProjection().pixelToRay(x, y);

        Matrix4f transformMatrix = transform.getGlobalMatrix();
        Vector3f origin = transformMatrix.transformPosition(cameraLocalRay.getOrigin());
        Vector3f direction = transformMatrix.transformDirection(cameraLocalRay.getDirection());

        return new Ray3f(origin, direction.normalize());
    }

    public Camera getCamera() {
        return camera;
    }

    public Transform getTransform() {
        return transform;
    }
}
