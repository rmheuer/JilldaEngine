package com.github.rmheuer.engine.core.ecs;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.entity.EntityRegistry;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.schedule.SystemScheduler;
import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.profile.Profiler;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.util.QuadConsumer;
import com.github.rmheuer.engine.core.util.TriConsumer;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

// TODO: Add names to worlds for debugging
public final class World {
    private final EntityRegistry registry;
    private final Entity root;
    private final Entity singletons;
    private final transient SystemScheduler scheduler;
    private final Queue<Event> eventQueue;

    public World(Set<Class<? extends GameSystem>> systems) {
        registry = new EntityRegistry();

        root = registry.newEntity("Root");
        singletons = registry.newEntity("Singletons");
        scheduler = new SystemScheduler();
        scheduler.setSystems(systems);
        eventQueue = new ConcurrentLinkedQueue<>();

        root.addComponent(new Transform());

        registry.setComponentAddListener((c) -> {
            Profiler profiler = Game.get().getProfiler();
            profiler.push("Component add: " + c.getClass().getSimpleName());
            scheduler.onComponentAdd(this, c);
            profiler.pop();
        });
        registry.setComponentRemoveListener((c) -> {
            Profiler profiler = Game.get().getProfiler();
            profiler.push("Component remove: " + c.getClass().getSimpleName());
            scheduler.onComponentRemove(this, c);
            profiler.pop();
        });
    }

    public void init() {
        scheduler.init(this);
    }

    public void postEvent(Event event) {
        eventQueue.add(event);
    }

    public void postImmediateEvent(Event event) {
        onEvent(event, true);
    }

    public void update(float delta) {
        Event event;
        while ((event = eventQueue.poll()) != null) {
            onEvent(event, false);
        }

        scheduler.update(this, delta);
    }

    public void fixedUpdate() {
        scheduler.fixedUpdate(this);
    }

    public void close() {
        scheduler.close(this);
    }

    private void onEvent(Event event, boolean immediate) {
        Profiler profiler = Game.get().getProfiler();
        String prefix = immediate ? "Imm. world event: " : "World event: ";
        profiler.push(prefix + event.getClass().getSimpleName());
        scheduler.onEvent(this, event);
        profiler.pop();
    }

    public Entity getRoot() {
        return root;
    }

    public void setLocalSingleton(WorldLocalSingleton s) {
        singletons.addComponent(s);
    }

    public <T extends WorldLocalSingleton> T getLocalSingleton(Class<T> type) {
        return singletons.getComponent(type);
    }

    public <T extends Component> T getOne(Class<T> type) {
        return registry.getOne(type);
    }

    public Set<Entity> getEntities() {
        return registry.getEntities();
    }

    public <A extends Component> void forEach(Class<A> a, Consumer<A> fn) {
        for (Entity entity : registry.getEntitiesWith(a)) {
            fn.accept(entity.getComponent(a));
        }
    }

    public <A extends Component,
            B extends Component> void forEach(Class<A> a, Class<B> b, BiConsumer<A, B> fn) {
        for (Entity entity : registry.getEntitiesWith(a, b)) {
            fn.accept(
                    entity.getComponent(a),
                    entity.getComponent(b)
            );
        }
    }

    public <A extends Component,
            B extends Component,
            C extends Component> void forEach(Class<A> a, Class<B> b, Class<C> c, TriConsumer<A, B, C> fn) {
        for (Entity entity : registry.getEntitiesWith(a, b, c)) {
            fn.accept(
                    entity.getComponent(a),
                    entity.getComponent(b),
                    entity.getComponent(c)
            );
        }
    }

    public <A extends Component,
            B extends Component,
            C extends Component,
            D extends Component> void forEach(Class<A> a, Class<B> b, Class<C> c, Class<D> d, QuadConsumer<A, B, C, D> fn) {
        for (Entity entity : registry.getEntitiesWith(a, b, c, d)) {
            fn.accept(
                    entity.getComponent(a),
                    entity.getComponent(b),
                    entity.getComponent(c),
                    entity.getComponent(d)
            );
        }
    }

    public <A extends Component> void forEachEntity(Class<A> a, BiConsumer<Entity, A> fn) {
        for (Entity entity : registry.getEntitiesWith(a)) {
            fn.accept(
                    entity,
                    entity.getComponent(a)
            );
        }
    }

    public <A extends Component,
            B extends Component> void forEachEntity(Class<A> a, Class<B> b, TriConsumer<Entity, A, B> fn) {
        for (Entity entity : registry.getEntitiesWith(a)) {
            fn.accept(
                    entity,
                    entity.getComponent(a),
                    entity.getComponent(b)
            );
        }
    }

    public <A extends Component,
            B extends Component,
            C extends Component> void forEachEntity(Class<A> a, Class<B> b, Class<C> c, QuadConsumer<Entity, A, B, C> fn) {
        for (Entity entity : registry.getEntitiesWith(a)) {
            fn.accept(
                    entity,
                    entity.getComponent(a),
                    entity.getComponent(b),
                    entity.getComponent(c)
            );
        }
    }
}
