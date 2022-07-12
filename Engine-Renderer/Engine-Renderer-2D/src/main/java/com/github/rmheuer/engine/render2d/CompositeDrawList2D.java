package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.render.texture.Texture;
import com.github.rmheuer.engine.render2d.font.Font;

import java.util.Arrays;
import java.util.List;

public final class CompositeDrawList2D extends DrawList2D {
    private final float depthInterval;
    private float depth;

    public CompositeDrawList2D() {
        this(0.01f);
    }

    public CompositeDrawList2D(float depthInterval) {
        this.depthInterval = depthInterval;
        depth = 0;
    }

    @Override
    public void join(DrawList2D other) {
        if (other instanceof CompositeDrawList2D) {
            CompositeDrawList2D c = (CompositeDrawList2D) other;
            for (DrawVertex v : c.getVertices()) {
                v.setDepth(v.getDepth() + depth + depthInterval);
            }
            depth += depthInterval + c.depthInterval * c.getVertices().size();
        }

        super.join(other);
    }

    private void step() {
        depth += depthInterval;
    }

    public void drawText(String text, Vector2f pos, Vector2f align, Font font, Vector4f color) { drawText(text, pos.x, pos.y, align.x, align.y, font, color); }
    public void drawText(String text, Vector2f pos, float alignX, float alignY, Font font, Vector4f color) { drawText(text, pos.x, pos.y, alignX, alignY, font, color); }
    public void drawText(String text, float x, float y, Vector2f align, Font font, Vector4f color) { drawText(text, x, y, align.x, align.y, font, color); }
    public void drawText(String text, float x, float y, float alignX, float alignY, Font font, Vector4f color) { step(); drawText(depth, text, x, y, alignX, alignY, font, color); }
    public void drawText(String text, Vector2f pos, Font font, Vector4f color) { drawText(text, pos.x, pos.y, font, color); }
    public void drawText(String text, float x, float y, Font font, Vector4f color) { step(); drawText(depth, text, x, y, font, color); }

    public void drawLineStrip(Vector2f[] points, float width, Vector4f color) { drawLineStrip(Arrays.asList(points), width, color); }
    public void drawLineStrip(List<Vector2f> points, float width, Vector4f color) { step(); drawLineStrip(depth, points, width, color); }

    public void fillConvexPolygon(Vector2f[] points, Vector4f color) { fillConvexPolygon(Arrays.asList(points), color); }
    public void fillConvexPolygon(List<Vector2f> points, Vector4f color) { step(); fillConvexPolygon(depth, points, color); }

    public void drawLine(Vector2f p1, Vector2f p2, float width, Vector4f color) { drawLine(p1.x, p1.y, p2.x, p2.y, width, color); }
    public void drawLine(Vector2f p1, float x2, float y2, float width, Vector4f color) { drawLine(p1.x, p1.y, x2, y2, width, color); }
    public void drawLine(float x1, float y1, Vector2f p2, float width, Vector4f color) { drawLine(x1, y1, p2.x, p2.y, width, color); }
    public void drawLine(float x1, float y1, float x2, float y2, float width, Vector4f color) { step(); drawLine(depth, x1, y1, x2, y2, width, color); }

    public void drawQuad(Rectangle r, Vector4f color, float width) { drawQuad(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), width, color); }
    public void drawQuad(Vector2f pos, Vector2f size, float width, Vector4f color) { drawQuad(pos.x, pos.y, size.x, size.y, width, color); }
    public void drawQuad(Vector2f pos, float w, float h, float width, Vector4f color) { drawQuad(pos.x, pos.y, w, h, width, color); }
    public void drawQuad(float x, float y, Vector2f size, float width, Vector4f color) { drawQuad(x, y, size.x, size.y, width, color); }
    public void drawQuad(float x, float y, float w, float h, float width, Vector4f color) { step(); drawQuad(depth, x, y, w, h, width, color); }

    public void fillQuad(Rectangle r, Vector4f color) { fillQuad(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), color); }
    public void fillQuad(Vector2f pos, Vector2f size, Vector4f color) { fillQuad(pos.x, pos.y, size.x, size.y, color); }
    public void fillQuad(Vector2f pos, float w, float h, Vector4f color) { fillQuad(pos.x, pos.y, w, h, color); }
    public void fillQuad(float x, float y, Vector2f size, Vector4f color) { fillQuad(x, y, size.x, size.y, color); }
    public void fillQuad(float x, float y, float w, float h, Vector4f color) { step(); fillQuad(depth, x, y, w, h, color); }

    public void drawRoundedQuad(Rectangle r, float rad, float width, Vector4f color) { drawRoundedQuad(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(Vector2f pos, Vector2f size, float rad, float width, Vector4f color) { drawRoundedQuad(pos.x, pos.y, size.x, size.y, rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(Vector2f pos, float w, float h, float rad, float width, Vector4f color) { drawRoundedQuad(pos.x, pos.y, w, h, rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(float x, float y, Vector2f size, float rad, float width, Vector4f color) { drawRoundedQuad(x, y, size.x, size.y, rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(float x, float y, float w, float h, float rad, float width, Vector4f color) { drawRoundedQuad(x, y, w, h, rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(Rectangle r, float ul, float ur, float ll, float lr, float width, Vector4f color) { drawRoundedQuad(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), ul, ur, ll, lr, width, color); }
    public void drawRoundedQuad(Vector2f pos, Vector2f size, float ul, float ur, float ll, float lr, float width, Vector4f color) { drawRoundedQuad(pos.x, pos.y, size.x, size.y, ul, ur, ll, lr, width, color); }
    public void drawRoundedQuad(Vector2f pos, float w, float h, float ul, float ur, float ll, float lr, float width, Vector4f color) { drawRoundedQuad(pos.x, pos.y, w, h, ul, ur, ll, lr, width, color); }
    public void drawRoundedQuad(float x, float y, Vector2f size, float ul, float ur, float ll, float lr, float width, Vector4f color) { drawRoundedQuad(x, y, size.x, size.y, ul, ur, ll, lr, width, color); }
    public void drawRoundedQuad(float x, float y, float w, float h, float ul, float ur, float ll, float lr, float width, Vector4f color) { step(); drawRoundedQuad(depth, x, y, w, h, ul, ur, ll, lr, width, color); }

    public void fillRoundedQuad(Rectangle r, float rad, Vector4f color) { fillRoundedQuad(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), rad, rad, rad, rad, color); }
    public void fillRoundedQuad(Vector2f pos, Vector2f size, float rad, Vector4f color) { fillRoundedQuad(pos.x, pos.y, size.x, size.y, rad, rad, rad, rad, color); }
    public void fillRoundedQuad(Vector2f pos, float w, float h, float rad, Vector4f color) { fillRoundedQuad(pos.x, pos.y, w, h, rad, rad, rad, rad, color); }
    public void fillRoundedQuad(float x, float y, Vector2f size, float rad, Vector4f color) { fillRoundedQuad(x, y, size.x, size.y, rad, rad, rad, rad, color); }
    public void fillRoundedQuad(float x, float y, float w, float h, float rad, Vector4f color) { fillRoundedQuad(x, y, w, h, rad, rad, rad, rad, color); }
    public void fillRoundedQuad(Rectangle r, float ul, float ur, float ll, float lr, Vector4f color) { fillRoundedQuad(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), ul, ur, ll, lr, color); }
    public void fillRoundedQuad(Vector2f pos, Vector2f size, float ul, float ur, float ll, float lr, Vector4f color) { fillRoundedQuad(pos.x, pos.y, size.x, size.y, ul, ur, ll, lr, color); }
    public void fillRoundedQuad(Vector2f pos, float w, float h, float ul, float ur, float ll, float lr, Vector4f color) { fillRoundedQuad(pos.x, pos.y, w, h, ul, ur, ll, lr, color); }
    public void fillRoundedQuad(float x, float y, Vector2f size, float ul, float ur, float ll, float lr, Vector4f color) { fillRoundedQuad(x, y, size.x, size.y, ul, ur, ll, lr, color); }
    public void fillRoundedQuad(float x, float y, float w, float h, float ul, float ur, float ll, float lr, Vector4f color) { step(); fillRoundedQuad(depth, x, y, w, h, ul, ur, ll, lr, color); }

    public void drawTriangle(Vector2f p1, Vector2f p2, Vector2f p3, float width, Vector4f color) { drawTriangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, width, color); }
    public void drawTriangle(Vector2f p1, Vector2f p2, float x3, float y3, float width, Vector4f color) { drawTriangle(p1.x, p1.y, p2.x, p2.y, x3, y3, width, color); }
    public void drawTriangle(Vector2f p1, float x2, float y2, Vector2f p3, float width, Vector4f color) { drawTriangle(p1.x, p1.y, x2, y2, p3.x, p3.y, width, color); }
    public void drawTriangle(Vector2f p1, float x2, float y2, float x3, float y3, float width, Vector4f color) { drawTriangle(p1.x, p1.y, x2, y2, x3, y3, width, color); }
    public void drawTriangle(float x1, float y1, Vector2f p2, Vector2f p3, float width, Vector4f color) { drawTriangle(x1, y1, p2.x, p2.y, p3.x, p3.y, width, color); }
    public void drawTriangle(float x1, float y1, Vector2f p2, float x3, float y3, float width, Vector4f color) { drawTriangle(x1, y1, p2.x, p2.y, x3, y3, width, color); }
    public void drawTriangle(float x1, float y1, float x2, float y2, Vector2f p3, float width, Vector4f color) { drawTriangle(x1, y1, x2, y2, p3.x, p3.y, width, color); }
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float width, Vector4f color) { step(); drawTriangle(depth, x1, y1, x2, y2, x3, y3, width, color); }

    public void fillTriangle(Vector2f p1, Vector2f p2, Vector2f p3, Vector4f color) { fillTriangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, color); }
    public void fillTriangle(Vector2f p1, Vector2f p2, float x3, float y3, Vector4f color) { fillTriangle(p1.x, p1.y, p2.x, p2.y, x3, y3, color); }
    public void fillTriangle(Vector2f p1, float x2, float y2, Vector2f p3, Vector4f color) { fillTriangle(p1.x, p1.y, x2, y2, p3.x, p3.y, color); }
    public void fillTriangle(Vector2f p1, float x2, float y2, float x3, float y3, Vector4f color) { fillTriangle(p1.x, p1.y, x2, y2, x3, y3, color); }
    public void fillTriangle(float x1, float y1, Vector2f p2, Vector2f p3, Vector4f color) { fillTriangle(x1, y1, p2.x, p2.y, p3.x, p3.y, color); }
    public void fillTriangle(float x1, float y1, Vector2f p2, float x3, float y3, Vector4f color) { fillTriangle(x1, y1, p2.x, p2.y, x3, y3, color); }
    public void fillTriangle(float x1, float y1, float x2, float y2, Vector2f p3, Vector4f color) { fillTriangle(x1, y1, x2, y2, p3.x, p3.y, color); }
    public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Vector4f color) { step(); fillTriangle(depth, x1, y1, x2, y2, x3, y3, color); }

    public void drawImage(Rectangle r, Texture img, Rectangle uvs) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(Rectangle r, Texture img, Vector2f uv0, Vector2f uv1) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(Rectangle r, Texture img, Vector2f uv0, float u1, float v1) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(Rectangle r, Texture img, float u0, float v0, Vector2f uv1) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(Rectangle r, Texture img, float u0, float v0, float u1, float v1) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, u0, v0, u1, v1); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, Rectangle uvs) { drawImage(pos.x, pos.y, size.x, size.y, img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, Rectangle uvs) { drawImage(pos.x, pos.y, w, h, img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float x, float y, Vector2f size, Texture img, Rectangle uvs) { drawImage(x, y, size.x, size.y, img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float x, float y, float w, float h, Texture img, Rectangle uvs) { drawImage(x, y, w, h, img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, Vector2f uv0, Vector2f uv1) { drawImage(pos.x, pos.y, size.x, size.y, img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, Vector2f uv0, float u1, float v1) { drawImage(pos.x, pos.y, size.x, size.y, img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, float u0, float v0, Vector2f uv1) { drawImage(pos.x, pos.y, size.x, size.y, img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, float u0, float v0, float u1, float v1) { drawImage(pos.x, pos.y, size.x, size.y, img, u0, v0, u1, v1); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, Vector2f uv0, Vector2f uv1) { drawImage(pos.x, pos.y, w, h, img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, Vector2f uv0, float u1, float v1) { drawImage(pos.x, pos.y, w, h, img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, float u0, float v0, Vector2f uv1) { drawImage(pos.x, pos.y, w, h, img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, float u0, float v0, float u1, float v1) { drawImage(pos.x, pos.y, w, h, img, u0, v0, u1, v1); }
    public void drawImage(float x, float y, Vector2f size, Texture img, Vector2f uv0, Vector2f uv1) { drawImage(x, y, size.x, size.y, img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float x, float y, Vector2f size, Texture img, Vector2f uv0, float u1, float v1) { drawImage(x, y, size.x, size.y, img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float x, float y, Vector2f size, Texture img, float u0, float v0, Vector2f uv1) { drawImage(x, y, size.x, size.y, img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float x, float y, Vector2f size, Texture img, float u0, float v0, float u1, float v1) { drawImage(x, y, size.x, size.y, img, u0, v0, u1, v1); }
    public void drawImage(float x, float y, float w, float h, Texture img, Vector2f uv0, Vector2f uv1) { drawImage(x, y, w, h, img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float x, float y, float w, float h, Texture img, Vector2f uv0, float u1, float v1) { drawImage(x, y, w, h, img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float x, float y, float w, float h, Texture img, float u0, float v0, Vector2f uv1) { drawImage(x, y, w, h, img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float x, float y, float w, float h, Texture img, float u0, float v0, float u1, float v1) { drawImage(x, y, w, h, img, new Vector4f(1, 1, 1, 1), u0, v0, u1, v1); }
    public void drawImage(Rectangle r, Texture img, Vector4f tint, Rectangle uvs) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(Rectangle r, Texture img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(Rectangle r, Texture img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(Rectangle r, Texture img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(Rectangle r, Texture img, Vector4f tint, float u0, float v0, float u1, float v1) { drawImage(r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, u0, v0, u1, v1); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, Vector4f tint, Rectangle uvs) { drawImage(pos.x, pos.y, size.x, size.y, img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, Vector4f tint, Rectangle uvs) { drawImage(pos.x, pos.y, w, h, img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float x, float y, Vector2f size, Texture img, Vector4f tint, Rectangle uvs) { drawImage(x, y, size.x, size.y, img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float x, float y, float w, float h, Texture img, Vector4f tint, Rectangle uvs) { drawImage(x, y, w, h, img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(pos.x, pos.y, size.x, size.y, img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(pos.x, pos.y, size.x, size.y, img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(pos.x, pos.y, size.x, size.y, img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(Vector2f pos, Vector2f size, Texture img, Vector4f tint, float u0, float v0, float u1, float v1) { drawImage(pos.x, pos.y, size.x, size.y, img, tint, u0, v0, u1, v1); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(pos.x, pos.y, w, h, img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(pos.x, pos.y, w, h, img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(pos.x, pos.y, w, h, img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(Vector2f pos, float w, float h, Texture img, Vector4f tint, float u0, float v0, float u1, float v1) { drawImage(pos.x, pos.y, w, h, img, tint, u0, v0, u1, v1); }
    public void drawImage(float x, float y, Vector2f size, Texture img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(x, y, size.x, size.y, img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float x, float y, Vector2f size, Texture img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(x, y, size.x, size.y, img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float x, float y, Vector2f size, Texture img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(x, y, size.x, size.y, img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float x, float y, Vector2f size, Texture img, Vector4f tint, float u0, float v0, float u1, float v1) { drawImage(x, y, size.x, size.y, img, tint, u0, v0, u1, v1); }
    public void drawImage(float x, float y, float w, float h, Texture img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(x, y, w, h, img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float x, float y, float w, float h, Texture img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(x, y, w, h, img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float x, float y, float w, float h, Texture img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(x, y, w, h, img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float x, float y, float w, float h, Texture img, Vector4f tint, float u0, float v0, float u1, float v1) { step(); drawImage(depth, x, y, w, h, img, tint, u0, v0, u1, v1); }
}
