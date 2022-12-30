package com.github.rmheuer.engine.render3d.loader;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.render.mesh.MeshData;
import com.github.rmheuer.engine.render.mesh.VertexLayout;
import com.github.rmheuer.engine.render.shader.AttribType;

public final class DefaultVertexAdapter implements VertexAdapter {
    public static final VertexLayout LAYOUT = new VertexLayout(
            AttribType.VEC3, // Position
            AttribType.VEC2, // Texture coord
            AttribType.VEC3  // Normal
    );

    public static final DefaultVertexAdapter INSTANCE = new DefaultVertexAdapter();

    private DefaultVertexAdapter() {}

    @Override
    public void adapt(MeshData data, Vector3f position, Vector2f texCoord, Vector3f normal) {
        data.putVec3(position).putVec2(texCoord).putVec3(normal);
    }

    @Override
    public VertexLayout getLayout() {
        return LAYOUT;
    }
}
