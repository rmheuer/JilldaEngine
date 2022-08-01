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

    public EntityRegistry() {
        components = new HashMap<>();
        entities = new HashSet<>();
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
        Set<Entity> out = new HashSet<>();
        for (Map.Entry<Class<? extends Component>, Map<Entity, Component>> entry : components.entrySet()) {
            out.addAll(entry.getValue().keySet());
        }
        return out;
    }

    public void addComponent(Entity e, Component c) {
        Map<Entity, Component> map = components.computeIfAbsent(c.getClass(), (k) -> new HashMap<>());
        map.put(e, c);
    }

    public <T extends Component> T getComponent(Entity e, Class<T> type) {
        Map<Entity, Component> map = components.get(type);
        if (map == null)
            return null;

        @SuppressWarnings("unchecked")
        T t = (T) map.get(e);

        return t;
    }

    public void removeComponent(Entity e, Class<? extends Component> type) {
        Map<Entity, Component> map = components.get(type);
        if (map == null)
            return;

        map.remove(e);
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

    public void delete(Entity e) {
        if (!entities.contains(e))
            return;

        for (Map<Entity, Component> map : components.values()) {
            map.remove(e);
        }
        entities.remove(e);
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
