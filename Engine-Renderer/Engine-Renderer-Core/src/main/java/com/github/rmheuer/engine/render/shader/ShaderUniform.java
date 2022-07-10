package com.github.rmheuer.engine.render.shader;

import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.math.Vector4f;

public interface ShaderUniform {
    String getName();

    void setFloat(float f);
    void setVector2f(float x, float y);
    default void setVector2f(Vector2f vec) { setVector2f(vec.x, vec.y); }
    void setVector3f(float x, float y, float z);
    default void setVector3f(Vector3f vec) { setVector3f(vec.x, vec.y, vec.z); }
    void setVector4f(float x, float y, float z, float w);
    default void setVector4f(Vector4f vec) { setVector4f(vec.x, vec.y, vec.z, vec.w); }
    void setMatrix4f(Matrix4f mat);

    void setInt(int i);
}
