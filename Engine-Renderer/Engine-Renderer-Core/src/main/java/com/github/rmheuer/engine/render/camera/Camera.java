package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.math.Vector2f;

public final class Camera implements Component {
    private Projection projection;
    private Vector2f viewportMin, viewportMax; // Percentage from top-left corner

    public Camera(Projection projection) {
        this.projection = projection;
        viewportMin = new Vector2f(0, 0);
        viewportMax = new Vector2f(1, 1);
    }

    public Projection getProjection() {
	return projection;
    }

    public void setProjection(Projection projection) {
	this.projection = projection;
    }

    public Vector2f getViewportMin() {
        return viewportMin;
    }

    public Camera setViewportMin(Vector2f viewportMin) {
        this.viewportMin = viewportMin;
        return this;
    }

    public Vector2f getViewportMax() {
        return viewportMax;
    }

    public Camera setViewportMax(Vector2f viewportMax) {
        this.viewportMax = viewportMax;
        return this;
    }
}
