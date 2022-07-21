package com.github.rmheuer.engine.render.event;

import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.camera.Camera;

public final class RenderSceneEvent implements Event {
    private final Camera camera;
    private final Transform cameraTransform;

    public RenderSceneEvent(Camera camera, Transform cameraTransform) {
        this.camera = camera;
        this.cameraTransform = cameraTransform;
    }

    public Camera getCamera() {
        return camera;
    }

    public Transform getCameraTransform() {
        return cameraTransform;
    }


    @Override
    public String toString() {
        return "RenderSceneEvent{" +
                "camera=" + camera +
                ", cameraTransform=" + cameraTransform +
                '}';
    }
}
