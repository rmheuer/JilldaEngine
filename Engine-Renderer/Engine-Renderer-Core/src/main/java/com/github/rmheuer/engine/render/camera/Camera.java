package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.ecs.component.Component;

public final class Camera implements Component {
    private Projection projection;

    public Camera(Projection projection) {
        this.projection = projection;
    }

    public Projection getProjection() {
	return projection;
    }

    public void setProjection(Projection projection) {
	this.projection = projection;
    }
}
