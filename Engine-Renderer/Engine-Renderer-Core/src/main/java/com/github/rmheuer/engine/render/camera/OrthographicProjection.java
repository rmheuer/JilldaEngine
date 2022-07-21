package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.math.Matrix4f;

public final class OrthographicProjection implements Projection {
    private final Matrix4f matrix;

    public OrthographicProjection() {
    	matrix = new Matrix4f();
    }

    @Override
    public Matrix4f getMatrix() {
	return matrix;
    }

    @Override
    public void resize(int width, int height) {
	// If the size is even, the origin will be the upper-left
	// center pixel. If it is odd, it will be the center pixel.
	matrix.ortho(
		     (float) -Math.floor(width / 2.0f),
		     (float) Math.ceil(width / 2.0f),
		     (float) Math.ceil(height / 2.0f),
		     (float) -Math.floor(height / 2.0f),
		     1000, -1000
	);
    }
}
