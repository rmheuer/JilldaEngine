package com.github.rmheuer.engine.core.ecs.entity;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.serial.node.SerialArray;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializeWith;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SerializeWith(Entity.Serializer.class)
public final class Entity {
	public static final class Serializer implements TypeCodec<Entity> {
		@Override
		public SerialNode serialize(Entity entity, SerializationContext ctx) {
			SerialArray arr = new SerialArray();
			for (Component c : entity.getComponents()) {
				arr.add(ctx.serialize(c, Component.class));
			}
			return arr;
		}

		@Override
		public Entity deserialize(SerialNode node, DeserializationContext ctx) {
			EntityRegistry reg = (EntityRegistry) ctx.getUserData();
			Entity e = reg.newEntity();

			for (SerialNode n : (SerialArray) node) {
				e.addComponent(ctx.deserialize(n, Component.class));
			}

			return e;
		}
	}

	private final EntityRegistry registry;

	public Entity(EntityRegistry registry) {
		this.registry = registry;

		addComponent(new Hierarchy());
	}

	public void addComponent(Component c) {
		registry.addComponent(this, c);
	}

	public void addComponents(Component... components) {
		for (Component c : components)
			addComponent(c);
	}

	public <T extends Component> T getComponent(Class<T> type) {
		return registry.getComponent(this, type);
	}

	public void removeComponent(Class<? extends Component> type) {
		registry.removeComponent(this, type);
	}

	public boolean hasComponent(Class<? extends Component> type) {
		return registry.hasComponent(this, type);
	}

	public Set<Component> getComponents() {
		return registry.getComponents(this);
	}

	public Entity newChild(Component... components) {
		Entity child = registry.newEntity();
		setupChild(child);
		child.addComponents(components);
		return child;
	}

	public Entity newChild(String name, Component... components) {
		Entity child = registry.newEntity(name);
		setupChild(child);
		child.addComponents(components);
		return child;
	}

	private void setupChild(Entity child) {
		Hierarchy hierarchy = getComponent(Hierarchy.class);
		Hierarchy childHierarchy = child.getComponent(Hierarchy.class);

		hierarchy.getChildren().add(child);
		childHierarchy.setParent(this);
	}

	public void addChild(Entity child) {
		Hierarchy childHierarchy = child.getComponent(Hierarchy.class);
		if (childHierarchy.getParent() != null) {
			Entity parent = childHierarchy.getParent();
			Hierarchy parentHierarchy = parent.getComponent(Hierarchy.class);

			parentHierarchy.getChildren().remove(child);
		}

		Hierarchy hierarchy = getComponent(Hierarchy.class);
		childHierarchy.setParent(this);
		hierarchy.getChildren().add(child);
	}

	public List<Entity> getChildren() {
		return new ArrayList<>(getComponent(Hierarchy.class).getChildren());
	}

	public String getName() {
		return getComponent(Name.class).getName();
	}

	public void delete() {
		Hierarchy hierarchy = getComponent(Hierarchy.class);
		for (Entity child : new ArrayList<>(hierarchy.getChildren())) {
			child.delete();
		}

		Entity parent = hierarchy.getParent();
		if (parent != null) {
			Hierarchy parentHierarchy = parent.getComponent(Hierarchy.class);
			parentHierarchy.getChildren().remove(this);
		}

		registry.delete(this);
	}
}
