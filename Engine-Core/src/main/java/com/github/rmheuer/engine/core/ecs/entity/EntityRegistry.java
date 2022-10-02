package com.github.rmheuer.engine.core.ecs.entity;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.serial.node.SerialArray;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializeWith;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@SerializeWith(EntityRegistry.Serializer.class)
public final class EntityRegistry {
    public static final class Serializer implements TypeCodec<EntityRegistry> {
        @Override
        public SerialNode serialize(EntityRegistry reg, SerializationContext ctx) {
            SerialArray arr = new SerialArray();
            for (Entity e : reg.entities) {
                arr.add(ctx.serialize(e));
            }
            return arr;
        }

        @Override
        public EntityRegistry deserialize(SerialNode node, DeserializationContext ctx) {
            EntityRegistry reg = new EntityRegistry();

            ctx.setUserData(reg);
            for (SerialNode n : (SerialArray) node) {
                // The Entity deserializer will create the entity and add components
                // itself, so the result doesn't matter here
                ctx.deserialize(n, Entity.class);
            }

            return reg;
        }
    }

    private final Map<Class<? extends Component>, Map<Entity, Component>> components;
    private final Set<Entity> entities;

    private Consumer<Component> componentAddListener, componentRemoveListener;

    public EntityRegistry() {
        components = new HashMap<>();
        entities = new HashSet<>();

        componentAddListener = (c) -> {};
        componentRemoveListener = (c) -> {};
    }

    public void setComponentAddListener(Consumer<Component> componentAddListener) {
        this.componentAddListener = componentAddListener;
    }

    public void setComponentRemoveListener(Consumer<Component> componentRemoveListener) {
        this.componentRemoveListener = componentRemoveListener;
    }

    public Entity newEntity() {
        return newEntity(new Name());
    }

    public Entity newEntity(String name) {
        return newEntity(new Name(name));
    }

    private Entity newEntity(Name name) {
        Entity entity = new Entity(this);
        entities.add(entity);
        addComponent(entity, name);
        return entity;
    }

    public Set<Entity> getEntities() {
        return new HashSet<>(entities);
    }

    private void doAddComponent(Entity e, Component c) {
        Map<Entity, Component> map = components.computeIfAbsent(c.getClass(), (k) -> new HashMap<>());
        Component prev = map.put(e, c);
        if (prev != null)
            componentRemoveListener.accept(prev);
        componentAddListener.accept(c);
    }

    public void addComponent(Entity e, Component c) {
            doAddComponent(e, c);
    }

    public <T extends Component> T getComponent(Entity e, Class<T> type) {
        Map<Entity, Component> map = components.get(type);
        if (map == null)
            return null;

        @SuppressWarnings("unchecked")
        T t = (T) map.get(e);

        return t;
    }

    private void doRemoveComponent(Entity e, Class<? extends Component> type) {
        Map<Entity, Component> map = components.get(type);
        if (map == null)
            return;

        Component removed = map.remove(e);
        if (removed != null)
            componentRemoveListener.accept(removed);
    }

    public void removeComponent(Entity e, Class<? extends Component> type) {
            doRemoveComponent(e, type);
    }

    public boolean hasComponent(Entity e, Class<? extends Component> type) {
        Map<Entity, Component> map = components.get(type);
        if (map == null)
            return false;

        return map.containsKey(e);
    }

    public Set<Component> getComponents(Entity e) {
        if (!entities.contains(e))
            return Collections.emptySet();

        Set<Component> out = new HashSet<>();
        for (Map<Entity, Component> map : components.values()) {
            Component c = map.get(e);
            if (c != null)
                out.add(c);
        }

        return out;
    }

    private void doDelete(Entity e) {
        if (!entities.contains(e))
            return;

        for (Entity child : e.getChildren()) {
            doDelete(child);
        }
        for (Map<Entity, Component> map : components.values()) {
            Component removed = map.remove(e);
            if (removed != null)
                componentRemoveListener.accept(removed);
        }
        entities.remove(e);
    }

    public void delete(Entity e) {
            doDelete(e);
    }

    public <T extends Component> T getOne(Class<T> type) {
        Map<Entity, Component> map = components.get(type);
        if (map == null || map.size() == 0)
            return null;

        @SuppressWarnings("unchecked")
        T t = (T) map.values().iterator().next();

        return t;
    }

    @SafeVarargs
    public final Set<Entity> getEntitiesWith(Class<? extends Component> c, Class<? extends Component>... rest) {
        Map<Entity, Component> firstMap = components.get(c);
        if (firstMap == null) {
            return Collections.emptySet();
        }

        Set<Entity> entities = new HashSet<>(firstMap.keySet());

        for (Class<? extends Component> type : rest) {
            entities.removeIf((entity) -> !hasComponent(entity, type));

            // Early out if none remain
            if (entities.isEmpty())
                break;
        }

        return entities;
    }
}
