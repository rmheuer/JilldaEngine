package com.github.rmheuer.engine.render3d.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.render.CullMode;
import com.github.rmheuer.engine.render.PolygonMode;
import com.github.rmheuer.engine.render.WindingOrder;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render3d.material.Material;

public final class MeshRenderer implements Component {
    private Mesh<?> mesh;
    private Material material;
    private CullMode cullMode;
    private WindingOrder windingOrder;
    private PolygonMode polygonMode;
    private boolean enabled;

    public MeshRenderer() {
        this(null, null);
    }

    public MeshRenderer(Mesh<?> mesh, Material material) {
        this(mesh, material, CullMode.BACK);
    }

    public MeshRenderer(Mesh<?> mesh, Material material, CullMode cullMode) {
        this(mesh, material, cullMode, WindingOrder.COUNTERCLOCKWISE);
    }

    public MeshRenderer(Mesh<?> mesh, Material material, CullMode cullMode, WindingOrder windingOrder) {
        this.mesh = mesh;
        this.material = material;
        this.cullMode = cullMode;
        this.windingOrder = windingOrder;
        this.polygonMode = PolygonMode.FILL;
        enabled = true;
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

    public CullMode getCullMode() {
        return cullMode;
    }

    public void setCullMode(CullMode cullMode) {
        this.cullMode = cullMode;
    }

    public WindingOrder getWindingOrder() {
        return windingOrder;
    }

    public void setWindingOrder(WindingOrder windingOrder) {
        this.windingOrder = windingOrder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public PolygonMode getPolygonMode() {
        return polygonMode;
    }

    public void setPolygonMode(PolygonMode polygonMode) {
        this.polygonMode = polygonMode;
    }
}
