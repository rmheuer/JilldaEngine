package com.github.rmheuer.engine.render.mesh;

import com.github.rmheuer.engine.core.nat.NativeObject;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.render.RenderBackend;

// Passing a MeshData instance into this mesh transfers ownership to the mesh, which can
// close the data at any time. Do not retain the builder after passing it into
// any constructor or method in this class.
public final class Mesh {
    private final PrimitiveType primType;

    private MeshData dataToUpload;
    private boolean hasData;
    private MeshDataUsage dataUsage;

    private Native nat;

    public Mesh(PrimitiveType primType) {
        this.primType = primType;
    }

    public Mesh(PrimitiveType primType, MeshData data, MeshDataUsage usage) {
        this(primType);
        this.dataToUpload = data;
        this.dataUsage = usage;
        hasData = true;
    }

    public PrimitiveType getPrimType() {
        return primType;
    }

    public void setData(MeshData data, MeshDataUsage usage) {
        dataUsage = usage;
        dataToUpload = data;
        hasData = true;
    }

    public boolean hasData() {
        return hasData;
    }

    public interface Native extends NativeObject {
        // Builder passed here should not be closed by the native implementation
        void setData(MeshData data, MeshDataUsage usage);

        void render();
    }

    public Native getNative(NativeObjectManager mgr) {
        if (!hasData)
            throw new IllegalStateException("Cannot get native of mesh with no data");

        if (nat == null) {
            nat = RenderBackend.get().createMeshNative(primType);
            mgr.registerObject(nat);
        }

        if (dataToUpload != null) {
            nat.setData(dataToUpload, dataUsage);
            dataToUpload.close();
            dataToUpload = null;
        }

        return nat;
    }
}
