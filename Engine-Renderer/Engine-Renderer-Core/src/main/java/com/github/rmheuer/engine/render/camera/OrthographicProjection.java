package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Ray3f;
import com.github.rmheuer.engine.core.math.Vector3f;

public final class OrthographicProjection implements Projection {
    private final Matrix4f matrix;

    private float left, right, top, bottom;
    private int screenWidth, screenHeight;

    public OrthographicProjection() {
        matrix = new Matrix4f();
    }

    @Override
    public Matrix4f getMatrix() {
        return matrix;
    }

    @Override
    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        // If the size is even, the origin will be the upper-left
        // center pixel. If it is odd, it will be the center pixel.
        matrix.ortho(
                left = (float) -Math.floor(width / 2.0f),
                right = (float) Math.ceil(width / 2.0f),
                bottom = (float) Math.ceil(height / 2.0f),
                top = (float) -Math.floor(height / 2.0f),
                1000, -1000
        );
    }

    @Override
    public Ray3f pixelToRay(float pixelX, float pixelY) {
        float x = MathUtils.map(pixelX, 0, screenWidth, left, right);
        float y = MathUtils.map(pixelY, 0, screenHeight, top, bottom);
        return new Ray3f(new Vector3f(x, y, 0), new Vector3f(0, 0, 1));
    }
}
