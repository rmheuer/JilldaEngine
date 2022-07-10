package com.github.rmheuer.engine.render.camera;

import com.github.rmheuer.engine.core.math.Matrix4f;

public interface Projection {
    Matrix4f getMatrix();

    void resize(int width, int height);
}
