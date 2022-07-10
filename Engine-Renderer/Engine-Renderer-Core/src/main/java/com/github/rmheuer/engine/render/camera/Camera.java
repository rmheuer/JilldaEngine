package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.render.framebuffer.Framebuffer;

public final class Camera implements Component {
    private Projection projection;
    private transient Framebuffer framebuffer;

    public Camera(Projection projection) {
	    this.projection = projection;
	    this.framebuffer = null;
    }

    public Camera(Projection projection, Framebuffer framebuffer) {
        this.projection = projection;
        this.framebuffer = null;
    }

    public Projection getProjection() {
	return projection;
    }

    public void setProjection(Projection projection) {
	this.projection = projection;
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public void setFramebuffer(Framebuffer framebuffer) {
        this.framebuffer = framebuffer;
    }
}
