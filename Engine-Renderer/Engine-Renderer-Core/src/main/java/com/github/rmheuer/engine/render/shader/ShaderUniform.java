package com.github.rmheuer.engine.render.shader;

import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.math.Vector4f;

public final class ShaderUniform {
    private final String name;
    private Object value;
    private boolean dirty;

    public ShaderUniform(String name) {
        this.name = name;
        dirty = false;
    }

    public String getName() {
        return name;
    }

    public boolean isFloat() {
        return value instanceof Float;
    }

    public float getFloat() {
        return (float) value;
    }

    public void setFloat(float f) {
        value = f;
        dirty = true;
    }

    public boolean isVector2f() {
        return value instanceof Vector2f;
    }

    public Vector2f getVector2f() {
        return (Vector2f) value;
    }

    public void setVector2f(float x, float y) {
        setVector2f(new Vector2f(x, y));
    }

    public void setVector2f(Vector2f vec) {
        value = vec;
        dirty = true;
    }

    public boolean isVector3f() {
        return value instanceof Vector3f;
    }

    public Vector3f getVector3f() {
        return (Vector3f) value;
    }

    public void setVector3f(float x, float y, float z) {
        setVector3f(new Vector3f(x, y, z));
    }

    public void setVector3f(Vector3f vec) {
        value = vec;
        dirty = true;
    }

    public boolean isVector4f() {
        return value instanceof Vector3f;
    }

    public Vector4f getVector4f() {
        return (Vector4f) value;
    }

    public void setVector3f(float x, float y, float z, float w) {
        setVector4f(new Vector4f(x, y, z, w));
    }

    public void setVector4f(Vector4f vec) {
        value = vec;
        dirty = true;
    }

    public boolean isMatrix4f() {
        return value instanceof Matrix4f;
    }

    public Matrix4f getMatrix4f() {
        return (Matrix4f) value;
    }

    public void setMatrix4f(Matrix4f mat) {
        value = mat;
        dirty = true;
    }

    public boolean isInt() {
        return value instanceof Integer;
    }

    public int getInt() {
        return (int) value;
    }

    public void setInt(int i) {
        value = i;
        dirty = true;
    }

    // --- For internal use ---

    public boolean isDirty() {
        return dirty;
    }

    public void clearDirty() {
        dirty = false;
    }
}
