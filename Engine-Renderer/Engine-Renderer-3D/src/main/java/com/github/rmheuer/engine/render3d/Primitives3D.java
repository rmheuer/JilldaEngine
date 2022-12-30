package com.github.rmheuer.engine.render3d;

import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshData;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render3d.loader.DefaultVertexAdapter;

// Maybe make into an enum?
public final class Primitives3D {
    public static final Mesh CUBE = createCubeMesh();

    private static void cubeFace(MeshData data, Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4, float nx, float ny, float nz) {
        data.mark();

        Vector3f normal = new Vector3f(nx, ny, nz);
        data.putVec3(p1).putVec2(0, 0).putVec3(normal);
        data.putVec3(p2).putVec2(1, 0).putVec3(normal);
        data.putVec3(p3).putVec2(1, 1).putVec3(normal);
        data.putVec3(p4).putVec2(0, 1).putVec3(normal);
        data.indices(0, 2, 1, 0, 3, 2);
    }

    private static Mesh createCubeMesh() {
        MeshData data = new MeshData(DefaultVertexAdapter.LAYOUT);

        Vector3f ppp = new Vector3f( 0.5f,  0.5f,  0.5f);
        Vector3f ppn = new Vector3f( 0.5f,  0.5f, -0.5f);
        Vector3f pnp = new Vector3f( 0.5f, -0.5f,  0.5f);
        Vector3f pnn = new Vector3f( 0.5f, -0.5f, -0.5f);
        Vector3f npp = new Vector3f(-0.5f,  0.5f,  0.5f);
        Vector3f npn = new Vector3f(-0.5f,  0.5f, -0.5f);
        Vector3f nnp = new Vector3f(-0.5f, -0.5f,  0.5f);
        Vector3f nnn = new Vector3f(-0.5f, -0.5f, -0.5f);

        cubeFace(data, npn, ppn, ppp, npp,  0,  1,  0); // Top
        cubeFace(data, pnn, nnn, nnp, pnp,  0, -1,  0); // Bottom
        cubeFace(data, npp, ppp, pnp, nnp,  0,  0,  1); // Front
        cubeFace(data, ppn, npn, nnn, pnn,  0,  0, -1); // Back
        cubeFace(data, npn, npp, nnp, nnn, -1,  0,  0); // Left
        cubeFace(data, ppp, ppn, pnn, pnp,  1,  0,  0); // Right

        return new Mesh(PrimitiveType.TRIANGLES, data, MeshDataUsage.STATIC);
    }

    private Primitives3D() {
        throw new AssertionError();
    }
}
