package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Transform;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.render.texture.Texture2D;
import com.github.rmheuer.engine.render2d.font.Font;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class DrawList2D {
    private static final int CURVE_PRECISION = 8;
    private static final float[] curveLookup = new float[CURVE_PRECISION * 2 + 2];

    static {
        for (int i = 0; i <= CURVE_PRECISION; i++) {
            double angle = i / (double) CURVE_PRECISION * Math.PI / 2;

            curveLookup[i * 2] = (float) Math.cos(angle);
            curveLookup[i * 2 + 1] = (float) Math.sin(angle);
        }
    }

    private final List<DrawVertex> vertices;
    private final List<Integer> indices;

    private final Deque<Rectangle> clipStack;
    private Rectangle clipRect;
    private Transform planeTransform;

    public DrawList2D() {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();
        clipStack = new ArrayDeque<>();
        clipRect = null;
        planeTransform = new Transform();
    }

    public void setPlaneTransform(Transform tx) {
        planeTransform = tx;
    }

    public void join(DrawList2D other) {
        int mark = vertices.size();
        vertices.addAll(other.vertices);
        for (int index : other.indices) {
            indices.add(index + mark);
        }
    }

    public List<DrawVertex> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public Transform getPlaneTransform() {
        return planeTransform;
    }

    public void pushClip(float x, float y, float w, float h) { pushClip(Rectangle.fromXYSizes(x, y, w, h)); }
    public void pushClip(float x, float y, Vector2f size) { pushClip(Rectangle.fromXYSizes(x, y, size.x, size.y)); }
    public void pushClip(Vector2f size, float w, float h) { pushClip(Rectangle.fromXYSizes(size.x, size.y, w, h)); }
    public void pushClip(Vector2f pos, Vector2f size) { pushClip(Rectangle.fromXYSizes(pos.x, pos.y, size.x, size.y)); }
    public void pushClip(Rectangle r) {
        if (clipRect != null) {
            clipStack.push(clipRect);
            clipRect = r;//clipRect.intersect(r);
        } else {
            clipRect = r;
        }
    }

    public void popClip() {
        clipRect = clipStack.pollFirst();
    }

    public void drawText(float depth, String text, Vector2f pos, Vector2f align, Font font, Vector4f color) { drawText(depth, text, pos.x, pos.y, align.x, align.y, font, color); }
    public void drawText(float depth, String text, Vector2f pos, float alignX, float alignY, Font font, Vector4f color) { drawText(depth, text, pos.x, pos.y, alignX, alignY, font, color); }
    public void drawText(float depth, String text, float x, float y, Vector2f align, Font font, Vector4f color) { drawText(depth, text, x, y, align.x, align.y, font, color); }
    public void drawText(float depth, String text, float x, float y, float alignX, float alignY, Font font, Vector4f color) {
        float width = font.textWidth(text);
        float ascent = font.getMetrics().getAscent();
        float height = font.getMetrics().getHeight();

        drawText(depth, text, x - width * alignX, y + ascent - height * alignY, font, color);
    }

    public void drawText(float depth, String text, Vector2f pos, Font font, Vector4f color) { drawText(depth, text, pos.x, pos.y, font, color); }
    public void drawText(float depth, String text, float x, float y, Font font, Vector4f color) {
        font.draw(this, depth, text, x, y, color);
    }

    public void drawLineStrip(float depth, Vector2f[] points, float width, Vector4f color) { drawLineStrip(depth, Arrays.asList(points), width, color); }
    public void drawLineStrip(float depth, List<Vector2f> points, float width, Vector4f color) {
        Vector2f prevPoint = null;
        for (Vector2f point : points) {
            if (prevPoint != null)
                drawLine(depth, prevPoint, point, width, color);

            prevPoint = point;
        }
    }

    public void fillConvexPolygon(float depth, Vector2f[] points, Vector4f color) { fillConvexPolygon(depth, Arrays.asList(points), color); }
    public void fillConvexPolygon(float depth, List<Vector2f> points, Vector4f color) {
        List<Vector2f> clipped = clipRect != null ? PolygonClipper.clip(points, clipRect) : points;

        int mark = vertices.size();
        boolean setFirst = false;
        int lastIndex = -1;

        for (int i = 0; i < clipped.size(); i++) {
            Vector2f point = clipped.get(i);
            vertices.add(new DrawVertex(depth, point.x, point.y, color));

            if (!setFirst) {
                setFirst = true;
            } else if (lastIndex < 0) {
                lastIndex = 1;
            } else {
                int temp = lastIndex;
                lastIndex++;
                indices.add(mark);
                indices.add(mark + temp);
                indices.add(mark + lastIndex);
            }
        }
    }

    public void drawLine(float depth, Vector2f p1, Vector2f p2, float width, Vector4f color) { drawLine(depth, p1.x, p1.y, p2.x, p2.y, width, color); }
    public void drawLine(float depth, Vector2f p1, float x2, float y2, float width, Vector4f color) { drawLine(depth, p1.x, p1.y, x2, y2, width, color); }
    public void drawLine(float depth, float x1, float y1, Vector2f p2, float width, Vector4f color) { drawLine(depth, x1, y1, p2.x, p2.y, width, color); }
    public void drawLine(float depth, float x1, float y1, float x2, float y2, float width, Vector4f color) {
        Vector2f pos1 = new Vector2f(x1 + 0.5f, y1 + 0.5f);
        Vector2f pos2 = new Vector2f(x2 + 0.5f, y2 + 0.5f);

        Vector2f dir = new Vector2f(pos2)
                .sub(pos1)
                .normalize()
                .mul(width / 2.0f);
        Vector2f perp = new Vector2f(-dir.y, dir.x);

        Vector2f p1 = new Vector2f(pos1).sub(dir).sub(perp);
        Vector2f p2 = new Vector2f(pos1).sub(dir).add(perp);
        Vector2f p3 = new Vector2f(pos2).add(dir).add(perp);
        Vector2f p4 = new Vector2f(pos2).add(dir).sub(perp);

        fillConvexPolygon(depth, new Vector2f[] {p1, p2, p3, p4}, color);
    }

    public void drawQuad(float depth, Rectangle r, float width, Vector4f color) { drawQuad(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), width, color); }
    public void drawQuad(float depth, Vector2f pos, Vector2f size, float width, Vector4f color) { drawQuad(depth, pos.x, pos.y, size.x, size.y, width, color); }
    public void drawQuad(float depth, Vector2f pos, float w, float h, float width, Vector4f color) { drawQuad(depth, pos.x, pos.y, w, h, width, color); }
    public void drawQuad(float depth, float x, float y, Vector2f size, float width, Vector4f color) { drawQuad(depth, x, y, size.x, size.y, width, color); }
    public void drawQuad(float depth, float x, float y, float w, float h, float width, Vector4f color) {
        drawLineStrip(depth, new Vector2f[] {
                new Vector2f(x, y),
                new Vector2f(x + w - 1, y),
                new Vector2f(x + w - 1, y + h - 1),
                new Vector2f(x, y + h - 1),
                new Vector2f(x, y) // loop around
        }, width, color);
    }

    public void fillQuad(float depth, Rectangle r, Vector4f color) { fillQuad(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), color); }
    public void fillQuad(float depth, Vector2f pos, Vector2f size, Vector4f color) { fillQuad(depth, pos.x, pos.y, size.x, size.y, color); }
    public void fillQuad(float depth, Vector2f pos, float w, float h, Vector4f color) { fillQuad(depth, pos.x, pos.y, w, h, color); }
    public void fillQuad(float depth, float x, float y, Vector2f size, Vector4f color) { fillQuad(depth, x, y, size.x, size.y, color); }
    public void fillQuad(float depth, float x, float y, float w, float h, Vector4f color) {
        fillConvexPolygon(depth, new Vector2f[] {
                new Vector2f(x, y),
                new Vector2f(x + w, y),
                new Vector2f(x + w, y + h),
                new Vector2f(x, y + h)
        }, color);
    }

    public void drawRoundedQuad(float depth, Rectangle r, float rad, float width, Vector4f color) { drawRoundedQuad(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(float depth, Vector2f pos, Vector2f size, float rad, float width, Vector4f color) { drawRoundedQuad(depth, pos.x, pos.y, size.x, size.y, rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(float depth, Vector2f pos, float w, float h, float rad, float width, Vector4f color) { drawRoundedQuad(depth, pos.x, pos.y, w, h, rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(float depth, float x, float y, Vector2f size, float rad, float width, Vector4f color) { drawRoundedQuad(depth, x, y, size.x, size.y, rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(float depth, float x, float y, float w, float h, float rad, float width, Vector4f color) { drawRoundedQuad(depth, x, y, w, h, rad, rad, rad, rad, width, color); }
    public void drawRoundedQuad(float depth, Rectangle r, float ul, float ur, float ll, float lr, float width, Vector4f color) { drawRoundedQuad(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), ul, ur, ll, lr, width, color); }
    public void drawRoundedQuad(float depth, Vector2f pos, Vector2f size, float ul, float ur, float ll, float lr, float width, Vector4f color) { drawRoundedQuad(depth, pos.x, pos.y, size.x, size.y, ul, ur, ll, lr, width, color); }
    public void drawRoundedQuad(float depth, Vector2f pos, float w, float h, float ul, float ur, float ll, float lr, float width, Vector4f color) { drawRoundedQuad(depth, pos.x, pos.y, w, h, ul, ur, ll, lr, width, color); }
    public void drawRoundedQuad(float depth, float x, float y, Vector2f size, float ul, float ur, float ll, float lr, float width, Vector4f color) { drawRoundedQuad(depth, x, y, size.x, size.y, ul, ur, ll, lr, width, color); }
    public void drawRoundedQuad(float depth, float x, float y, float w, float h, float ul, float ur, float ll, float lr, float width, Vector4f color) {
        float mx = x + w - 1;
        float my = y + h - 1;

        // Edges
        drawLine(depth, x + ul, y, mx - ur, y, width, color);
        drawLine(depth, x, y + ul, x, my - ll, width, color);
        drawLine(depth, x + ll, my, mx - lr, my, width, color);
        drawLine(depth, mx, y + ur, mx, my - lr, width, color);

        // Corners
        for (int i = 1; i <= CURVE_PRECISION; i++) {
            float lx = curveLookup[i * 2 - 2];
            float ly = curveLookup[i * 2 - 1];
            float px = curveLookup[i * 2];
            float py = curveLookup[i * 2 + 1];

            drawLine(depth, mx - lr + lr * lx, my - lr + lr * ly, mx - lr + lr * px, my - lr + lr * py, width, color);
            drawLine(depth, x + ul - ul * lx, y + ul - ul * ly, x + ul - ul * px, y + ul - ul * py, width, color);
            drawLine(depth, mx - ur + ur * lx, y + ur - ur * ly, mx - ur + ur * px, y + ur - ur * py, width, color);
            drawLine(depth, x + ll - ll * lx, my - ll + ll * ly, x + ll - ll * px, my - ll + ll * py, width, color);
        }
    }

    public void fillRoundedQuad(float depth, Rectangle r, float rad, Vector4f color) { fillRoundedQuad(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), rad, rad, rad, rad, color); }
    public void fillRoundedQuad(float depth, Vector2f pos, Vector2f size, float rad, Vector4f color) { fillRoundedQuad(depth, pos.x, pos.y, size.x, size.y, rad, rad, rad, rad, color); }
    public void fillRoundedQuad(float depth, Vector2f pos, float w, float h, float rad, Vector4f color) { fillRoundedQuad(depth, pos.x, pos.y, w, h, rad, rad, rad, rad, color); }
    public void fillRoundedQuad(float depth, float x, float y, Vector2f size, float rad, Vector4f color) { fillRoundedQuad(depth, x, y, size.x, size.y, rad, rad, rad, rad, color); }
    public void fillRoundedQuad(float depth, float x, float y, float w, float h, float rad, Vector4f color) { fillRoundedQuad(depth, x, y, w, h, rad, rad, rad, rad, color); }
    public void fillRoundedQuad(float depth, Rectangle r, float ul, float ur, float ll, float lr, Vector4f color) { fillRoundedQuad(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), ul, ur, ll, lr, color); }
    public void fillRoundedQuad(float depth, Vector2f pos, Vector2f size, float ul, float ur, float ll, float lr, Vector4f color) { fillRoundedQuad(depth, pos.x, pos.y, size.x, size.y, ul, ur, ll, lr, color); }
    public void fillRoundedQuad(float depth, Vector2f pos, float w, float h, float ul, float ur, float ll, float lr, Vector4f color) { fillRoundedQuad(depth, pos.x, pos.y, w, h, ul, ur, ll, lr, color); }
    public void fillRoundedQuad(float depth, float x, float y, Vector2f size, float ul, float ur, float ll, float lr, Vector4f color) { fillRoundedQuad(depth, x, y, size.x, size.y, ul, ur, ll, lr, color); }
    public void fillRoundedQuad(float depth, float x, float y, float w, float h, float ul, float ur, float ll, float lr, Vector4f color) {
        float maxX = x + w;
        float maxY = y + h;
        List<Vector2f> v = new ArrayList<>();

        // Bottom right
        for (int i = 0; i <= CURVE_PRECISION; i++) {
            float vx = curveLookup[i * 2] * lr + maxX - lr;
            float vy = curveLookup[i * 2 + 1] * lr + maxY - lr;
            v.add(new Vector2f(vx, vy));
        }

        // Bottom left
        for (int i = CURVE_PRECISION; i >= 0; i--) {
            float vx = x + ll - curveLookup[i * 2] * ll;
            float vy = curveLookup[i * 2 + 1] * ll + maxY - ll;
            v.add(new Vector2f(vx, vy));
        }

        // Top left
        for (int i = 0; i <= CURVE_PRECISION; i++) {
            float vx = x + ur - curveLookup[i * 2] * ul;
            float vy = y + ur - curveLookup[i * 2 + 1] * ul;
            v.add(new Vector2f(vx, vy));
        }

        // Top right
        for (int i = CURVE_PRECISION; i >= 0; i--) {
            float vx = curveLookup[i * 2] * ur + maxX - ur;
            float vy = y + ur - curveLookup[i * 2 + 1] * ur;
            v.add(new Vector2f(vx, vy));
        }

        fillConvexPolygon(depth, v, color);
    }

    public void drawTriangle(float depth, Vector2f p1, Vector2f p2, Vector2f p3, float width, Vector4f color) { drawTriangle(depth, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, width, color); }
    public void drawTriangle(float depth, Vector2f p1, Vector2f p2, float x3, float y3, float width, Vector4f color) { drawTriangle(depth, p1.x, p1.y, p2.x, p2.y, x3, y3, width, color); }
    public void drawTriangle(float depth, Vector2f p1, float x2, float y2, Vector2f p3, float width, Vector4f color) { drawTriangle(depth, p1.x, p1.y, x2, y2, p3.x, p3.y, width, color); }
    public void drawTriangle(float depth, Vector2f p1, float x2, float y2, float x3, float y3, float width, Vector4f color) { drawTriangle(depth, p1.x, p1.y, x2, y2, x3, y3, width, color); }
    public void drawTriangle(float depth, float x1, float y1, Vector2f p2, Vector2f p3, float width, Vector4f color) { drawTriangle(depth, x1, y1, p2.x, p2.y, p3.x, p3.y, width, color); }
    public void drawTriangle(float depth, float x1, float y1, Vector2f p2, float x3, float y3, float width, Vector4f color) { drawTriangle(depth, x1, y1, p2.x, p2.y, x3, y3, width, color); }
    public void drawTriangle(float depth, float x1, float y1, float x2, float y2, Vector2f p3, float width, Vector4f color) { drawTriangle(depth, x1, y1, x2, y2, p3.x, p3.y, width, color); }
    public void drawTriangle(float depth, float x1, float y1, float x2, float y2, float x3, float y3, float width, Vector4f color) {
        drawLineStrip(depth, new Vector2f[] {
                new Vector2f(x1, y1),
                new Vector2f(x2, y2),
                new Vector2f(x3, y3),
                new Vector2f(x1, y1)
        }, width, color);
    }

    public void fillTriangle(float depth, Vector2f p1, Vector2f p2, Vector2f p3, Vector4f color) { fillTriangle(depth, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, color); }
    public void fillTriangle(float depth, Vector2f p1, Vector2f p2, float x3, float y3, Vector4f color) { fillTriangle(depth, p1.x, p1.y, p2.x, p2.y, x3, y3, color); }
    public void fillTriangle(float depth, Vector2f p1, float x2, float y2, Vector2f p3, Vector4f color) { fillTriangle(depth, p1.x, p1.y, x2, y2, p3.x, p3.y, color); }
    public void fillTriangle(float depth, Vector2f p1, float x2, float y2, float x3, float y3, Vector4f color) { fillTriangle(depth, p1.x, p1.y, x2, y2, x3, y3, color); }
    public void fillTriangle(float depth, float x1, float y1, Vector2f p2, Vector2f p3, Vector4f color) { fillTriangle(depth, x1, y1, p2.x, p2.y, p3.x, p3.y, color); }
    public void fillTriangle(float depth, float x1, float y1, Vector2f p2, float x3, float y3, Vector4f color) { fillTriangle(depth, x1, y1, p2.x, p2.y, x3, y3, color); }
    public void fillTriangle(float depth, float x1, float y1, float x2, float y2, Vector2f p3, Vector4f color) { fillTriangle(depth, x1, y1, x2, y2, p3.x, p3.y, color); }
    public void fillTriangle(float depth, float x1, float y1, float x2, float y2, float x3, float y3, Vector4f color) {
        fillConvexPolygon(depth, new Vector2f[] {
                new Vector2f(x1, y1),
                new Vector2f(x2, y2),
                new Vector2f(x3, y3)
        }, color);
    }

    public void drawImage(float depth, Rectangle r, Texture2D img, Rectangle uvs) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, Rectangle r, Texture2D img, Vector2f uv0, Vector2f uv1) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, Rectangle r, Texture2D img, Vector2f uv0, float u1, float v1) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, Rectangle r, Texture2D img, float u0, float v0, Vector2f uv1) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, Rectangle r, Texture2D img, float u0, float v0, float u1, float v1) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, u0, v0, u1, v1); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, Rectangle uvs) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, Rectangle uvs) { drawImage(depth, pos.x, pos.y, w, h, img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, Rectangle uvs) { drawImage(depth, x, y, size.x, size.y, img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, Rectangle uvs) { drawImage(depth, x, y, w, h, img, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, Vector2f uv0, Vector2f uv1) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, Vector2f uv0, float u1, float v1) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, float u0, float v0, Vector2f uv1) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, float u0, float v0, float u1, float v1) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, u0, v0, u1, v1); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, Vector2f uv0, Vector2f uv1) { drawImage(depth, pos.x, pos.y, w, h, img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, Vector2f uv0, float u1, float v1) { drawImage(depth, pos.x, pos.y, w, h, img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, float u0, float v0, Vector2f uv1) { drawImage(depth, pos.x, pos.y, w, h, img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, float u0, float v0, float u1, float v1) { drawImage(depth, pos.x, pos.y, w, h, img, u0, v0, u1, v1); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, Vector2f uv0, Vector2f uv1) { drawImage(depth, x, y, size.x, size.y, img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, Vector2f uv0, float u1, float v1) { drawImage(depth, x, y, size.x, size.y, img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, float u0, float v0, Vector2f uv1) { drawImage(depth, x, y, size.x, size.y, img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, float u0, float v0, float u1, float v1) { drawImage(depth, x, y, size.x, size.y, img, u0, v0, u1, v1); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, Vector2f uv0, Vector2f uv1) { drawImage(depth, x, y, w, h, img, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, Vector2f uv0, float u1, float v1) { drawImage(depth, x, y, w, h, img, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, float u0, float v0, Vector2f uv1) { drawImage(depth, x, y, w, h, img, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, float u0, float v0, float u1, float v1) { drawImage(depth, x, y, w, h, img, new Vector4f(1, 1, 1, 1), u0, v0, u1, v1); }
    public void drawImage(float depth, Rectangle r, Texture2D img, Vector4f tint, Rectangle uvs) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, Rectangle r, Texture2D img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, Rectangle r, Texture2D img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, Rectangle r, Texture2D img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, Rectangle r, Texture2D img, Vector4f tint, float u0, float v0, float u1, float v1) { drawImage(depth, r.getMin().x, r.getMin().y, r.getWidth(), r.getHeight(), img, tint, u0, v0, u1, v1); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, Vector4f tint, Rectangle uvs) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, Vector4f tint, Rectangle uvs) { drawImage(depth, pos.x, pos.y, w, h, img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, Vector4f tint, Rectangle uvs) { drawImage(depth, x, y, size.x, size.y, img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, Vector4f tint, Rectangle uvs) { drawImage(depth, x, y, w, h, img, tint, uvs.getMin().x, uvs.getMin().y, uvs.getMax().x, uvs.getMax().y); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, Vector2f pos, Vector2f size, Texture2D img, Vector4f tint, float u0, float v0, float u1, float v1) { drawImage(depth, pos.x, pos.y, size.x, size.y, img, tint, u0, v0, u1, v1); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(depth, pos.x, pos.y, w, h, img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(depth, pos.x, pos.y, w, h, img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(depth, pos.x, pos.y, w, h, img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, Vector2f pos, float w, float h, Texture2D img, Vector4f tint, float u0, float v0, float u1, float v1) { drawImage(depth, pos.x, pos.y, w, h, img, tint, u0, v0, u1, v1); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(depth, x, y, size.x, size.y, img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(depth, x, y, size.x, size.y, img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(depth, x, y, size.x, size.y, img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, float x, float y, Vector2f size, Texture2D img, Vector4f tint, float u0, float v0, float u1, float v1) { drawImage(depth, x, y, size.x, size.y, img, tint, u0, v0, u1, v1); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, Vector4f tint, Vector2f uv0, Vector2f uv1) { drawImage(depth, x, y, w, h, img, tint, uv0.x, uv0.y, uv1.x, uv1.y); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, Vector4f tint, Vector2f uv0, float u1, float v1) { drawImage(depth, x, y, w, h, img, tint, uv0.x, uv0.y, u1, v1); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, Vector4f tint, float u0, float v0, Vector2f uv1) { drawImage(depth, x, y, w, h, img, tint, u0, v0, uv1.x, uv1.y); }
    public void drawImage(float depth, float x, float y, float w, float h, Texture2D img, Vector4f tint, float u0, float v0, float u1, float v1) {
        Rectangle rect = Rectangle.fromXYSizes(x, y, w, h);
        if (clipRect != null)
             rect = rect.intersect(clipRect);
        if (!rect.isValid())
            return;

        Vector2f min = rect.getMin();
        Vector2f max = rect.getMax();

        int mark = vertices.size();
        vertices.add(new DrawVertex(depth, min.x, min.y, MathUtils.map(min.x, x, x + w, u0, u1), MathUtils.map(min.y, y, y + h, v0, v1), tint, img));
        vertices.add(new DrawVertex(depth, max.x, min.y, MathUtils.map(max.x, x, x + w, u0, u1), MathUtils.map(min.y, y, y + h, v0, v1), tint, img));
        vertices.add(new DrawVertex(depth, max.x, max.y, MathUtils.map(max.x, x, x + w, u0, u1), MathUtils.map(max.y, y, y + h, v0, v1), tint, img));
        vertices.add(new DrawVertex(depth, min.x, max.y, MathUtils.map(min.x, x, x + w, u0, u1), MathUtils.map(max.y, y, y + h, v0, v1), tint, img));
        indices.add(mark);
        indices.add(mark + 1);
        indices.add(mark + 2);
        indices.add(mark);
        indices.add(mark + 2);
        indices.add(mark + 3);
    }
}
