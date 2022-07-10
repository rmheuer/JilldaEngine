package com.github.rmheuer.engine.render2d.font;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.render.texture.Texture;

public final class GlyphInfo {
    private final Texture tex;
    private final Vector2f size;
    private final Vector2f offset;
    private final float xAdvance;
    private final Vector2f uvMin;
    private final Vector2f uvMax;

    public GlyphInfo(Texture tex, Vector2f size, Vector2f offset, float xAdvance, Vector2f uvMin, Vector2f uvMax) {
        this.tex = tex;
        this.size = size;
        this.offset = offset;
        this.xAdvance = xAdvance;
        this.uvMin = uvMin;
        this.uvMax = uvMax;
    }

    public Texture getTexture() {
        return tex;
    }

    public Vector2f getSize() {
        return size;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public float getXAdvance() {
        return xAdvance;
    }

    public Vector2f getUVMin() {
        return uvMin;
    }

    public Vector2f getUVMax() {
        return uvMax;
    }
}
