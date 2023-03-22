package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Ray3f;

public interface Projection {
    Matrix4f getMatrix();

    void resize(float width, float height);

    Ray3f pixelToRay(float pixelX, float pixelY);
}
