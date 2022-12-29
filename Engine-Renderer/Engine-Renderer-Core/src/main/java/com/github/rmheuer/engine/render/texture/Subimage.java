package com.github.rmheuer.engine.render.texture;

import com.github.rmheuer.engine.core.math.Vector2f;

// TODO: Reflection and rotation
public class Subimage {
    protected Image src;
    private final int x, y;
    private final int width, height;

    public Subimage(Image src, int x, int y, int width, int height) {
        this.src = src;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Subimage getSubImage(int x, int y, int width, int height) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height)
            throw new IndexOutOfBoundsException("Position out of bounds");
        if (x + width > this.width || y + height > this.height)
            throw new IndexOutOfBoundsException("Size out of bounds");

        return new Subimage(src, this.x + x, this.y + y, width, height);
    }

    public Image getSourceImage() {
        return src;
    }

    public Vector2f getRegionMinUV() {
        return new Vector2f(x / (float) src.getWidth(), y / (float) src.getHeight());
    }

    public Vector2f getRegionMaxUV() {
        return new Vector2f((x + width) / (float) src.getWidth(), (y + height) / (float) src.getHeight());
    }
}
