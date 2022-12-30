package com.github.rmheuer.engine.render3d.loader;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.render.mesh.MeshData;
import com.github.rmheuer.engine.render.mesh.VertexLayout;

public interface VertexAdapter {
    void adapt(MeshData data, Vector3f position, Vector2f texCoord, Vector3f normal);

    VertexLayout getLayout();
}
