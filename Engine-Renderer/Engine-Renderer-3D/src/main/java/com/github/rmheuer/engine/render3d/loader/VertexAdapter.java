package com.github.rmheuer.engine.render3d.loader;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.render.mesh.Vertex;

@FunctionalInterface
public interface VertexAdapter<V extends Vertex> {
    V adapt(Vector3f position, Vector2f texCoord, Vector3f normal);
}
