package com.github.rmheuer.engine.render.mesh;

import com.github.rmheuer.engine.render.shader.AttribType;

public final class VertexLayout {
    private final AttribType[] types;

    public VertexLayout(AttribType... types) {
	this.types = types;
    }

    public AttribType[] getTypes() {
	return types;
    }
}
