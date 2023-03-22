package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Ray3f;
import com.github.rmheuer.engine.core.math.Vector3f;

public final class OrthographicProjection implements Projection {
    private static Bounds fromWidthHeight(float width, float height) {
        float left = (float) -Math.floor(width / 2.0f);
        float right = (float) Math.ceil(width / 2.0f);
        float bottom = (float) Math.ceil(height / 2.0f);
        float top = (float) -Math.floor(height / 2.0f);
        return new Bounds(left, right, top, bottom);
    }

    public enum ResizeRule {
        DYNAMIC {
            @Override
            Bounds apply(Bounds prev, float width, float height) {
                return fromWidthHeight(width, height);
            }
        },
        MAINTAIN_FIXED_WIDTH {
            @Override
            Bounds apply(Bounds prev, float width, float height) {
                int prevW = (int) (prev.right - prev.left);
                float scale = (float) prevW / width;

                return fromWidthHeight(prevW, (int) (height * scale));
            }
        },
        MAINTAIN_FIXED_HEIGHT {
            @Override
            Bounds apply(Bounds prev, float width, float height) {
                int prevH = (int) (prev.bottom - prev.top);
                float scale = (float) prevH / height;

                return fromWidthHeight((int) (width * scale), prevH);
            }
        };

        abstract Bounds apply(Bounds prev, float width, float height);
    }

    private static final class Bounds {
        private final float left;
        private final float right;
        private final float top;
        private final float bottom;

        public Bounds(float left, float right, float top, float bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }

    private final Matrix4f matrix;

    private ResizeRule resizeRule;
    private Bounds bounds;
    private float screenWidth, screenHeight;

    public OrthographicProjection() {
        this(ResizeRule.DYNAMIC);
    }

    public OrthographicProjection(ResizeRule resizeRule) {
        this.resizeRule = resizeRule;
        matrix = new Matrix4f();
    }

    @Override
    public Matrix4f getMatrix() {
        return matrix;
    }

    @Override
    public void resize(float width, float height) {
        screenWidth = width;
        screenHeight = height;

        if (bounds != null)
            bounds = resizeRule.apply(bounds, width, height);
        else
            // Initial bounds fits initial window size
            bounds = fromWidthHeight(width, height);

        // If the size is even, the origin will be the upper-left
        // center pixel. If it is odd, it will be the center pixel.
        matrix.ortho(
                bounds.left, bounds.right, bounds.bottom, bounds.top,
                1000, -1000
        );
    }

    @Override
    public Ray3f pixelToRay(float pixelX, float pixelY) {
        float x = MathUtils.map(pixelX, 0, screenWidth, bounds.left, bounds.right);
        float y = MathUtils.map(pixelY, 0, screenHeight, bounds.top, bounds.bottom);
        return new Ray3f(new Vector3f(x, y, 0), new Vector3f(0, 0, 1));
    }
}
