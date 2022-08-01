package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.render.texture.Image;

// Note: Not an actual vertex type!
//       This is the intermediate representation of the vertices
//       before being batched.
public final class DrawVertex {
    private float depth;
    private final float x, y;
    private final float u, v;
    private final Vector4f color;
    private final Image tex;

    public DrawVertex(float depth, float x, float y, Vector4f color) {
        this(depth, x, y, 0, 0, color, null);
    }

    public DrawVertex(float depth, float x, float y, float u, float v, Vector4f color, Image tex) {
        this.depth = depth;
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.color = color;
        this.tex = tex;
    }

    public float getDepth() {
        return depth;
    }

    // Settable for CompositeDrawList2D join
    public void setDepth(float depth) {
        this.depth = depth;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public Vector4f getColor() {
        return color;
    }

    public Image getTex() {
        return tex;
    }
}
