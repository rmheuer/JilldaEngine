package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.render.texture.Image;

// Note: Not an actual vertex type!
//       This is the intermediate representation of the vertices
//       before being batched.
public final class DrawVertex {
    private final Vector3f pos;
    private final float u, v;
    private final Vector4f color;
    private final Image tex;

    public DrawVertex(Vector3f pos, float u, float v, Vector4f color, Image tex) {
        this.pos = pos;
        this.u = u;
        this.v = v;
        this.color = color;
        this.tex = tex;
    }

    public Vector3f getPos() {
        return pos;
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
