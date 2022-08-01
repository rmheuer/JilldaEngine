package com.github.rmheuer.engine.render3d.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render3d.material.Material;

public final class MeshRenderer implements Component {
    private Mesh<?> mesh;
    private Material material;

    public MeshRenderer() {}

    public MeshRenderer(Mesh<?> mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    public Mesh<?> getMesh() {
        return mesh;
    }

    public void setMesh(Mesh<?> mesh) {
        this.mesh = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
