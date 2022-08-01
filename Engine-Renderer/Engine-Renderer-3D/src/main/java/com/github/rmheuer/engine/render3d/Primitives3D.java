package com.github.rmheuer.engine.render3d;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshBuilder;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render3d.loader.DefaultVertex;

// Maybe make into an enum?
public final class Primitives3D {
    public static final Mesh<DefaultVertex> CUBE = createCubeMesh();

    private static void cubeFace(MeshBuilder<DefaultVertex> builder, Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4, float nx, float ny, float nz) {
        builder.mark();

        Vector3f normal = new Vector3f(nx, ny, nz);
        builder.vertex(new DefaultVertex(p1, new Vector2f(0, 0), normal));
        builder.vertex(new DefaultVertex(p2, new Vector2f(1, 0), normal));
        builder.vertex(new DefaultVertex(p3, new Vector2f(1, 1), normal));
        builder.vertex(new DefaultVertex(p4, new Vector2f(0, 1), normal));
        builder.indices(0, 2, 1, 0, 3, 2);
    }

    private static Mesh<DefaultVertex> createCubeMesh() {
        MeshBuilder<DefaultVertex> builder = new MeshBuilder<>();

        Vector3f ppp = new Vector3f( 0.5f,  0.5f,  0.5f);
        Vector3f ppn = new Vector3f( 0.5f,  0.5f, -0.5f);
        Vector3f pnp = new Vector3f( 0.5f, -0.5f,  0.5f);
        Vector3f pnn = new Vector3f( 0.5f, -0.5f, -0.5f);
        Vector3f npp = new Vector3f(-0.5f,  0.5f,  0.5f);
        Vector3f npn = new Vector3f(-0.5f,  0.5f, -0.5f);
        Vector3f nnp = new Vector3f(-0.5f, -0.5f,  0.5f);
        Vector3f nnn = new Vector3f(-0.5f, -0.5f, -0.5f);

        cubeFace(builder, npn, ppn, ppp, npp,  0,  1,  0); // Top
        cubeFace(builder, pnn, nnn, nnp, pnp,  0, -1,  0); // Bottom
        cubeFace(builder, npp, ppp, pnp, nnp,  0,  0,  1); // Front
        cubeFace(builder, ppn, npn, nnn, pnn,  0,  0, -1); // Back
        cubeFace(builder, npn, npp, nnp, nnn, -1,  0,  0); // Left
        cubeFace(builder, ppp, ppn, pnn, pnp,  1,  0,  0); // Right

        return new Mesh<>(PrimitiveType.TRIANGLES, builder, MeshDataUsage.STATIC);
    }

    private Primitives3D() {
        throw new AssertionError();
    }
}
