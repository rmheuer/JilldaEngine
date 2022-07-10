package com.github.rmheuer.engine.core.ecs.entity;

import com.github.rmheuer.engine.core.ecs.component.Component;

import java.util.ArrayList;
import java.util.List;

public final class Hierarchy implements Component {
    private Entity parent;
    private List<Entity> children;

    public Hierarchy() {
	parent = null;
	children = new ArrayList<>();
    }
    
    public Entity getParent() {
	return parent;
    }

    public void setParent(Entity parent) {
	this.parent = parent;
    }

    public List<Entity> getChildren() {
	return children;
    }
}
