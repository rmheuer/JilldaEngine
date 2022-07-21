package com.github.rmheuer.engine.core.ecs;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.entity.EntityRegistry;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;
import com.github.rmheuer.engine.core.ecs.system.schedule.SystemScheduler;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.util.QuadConsumer;
import com.github.rmheuer.engine.core.util.TriConsumer;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class World {
    private final EntityRegistry registry;
    private final Entity root;
    private final Entity singletons;
    private final transient SystemScheduler scheduler;

    public World(Set<GameSystem> systems) {
        registry = new EntityRegistry();
        root = registry.newEntity("Root");
        singletons = registry.newEntity("Singletons");
        scheduler = new SystemScheduler(systems);

        root.addComponent(new Transform());
    }

    public void init() {
        scheduler.doStage(Stage.INIT, (sys) -> sys.init(this), false);
    }

    public void update(float delta) {
        scheduler.doStage(Stage.UPDATE, (sys) -> sys.update(this, delta), true);
    }

    public void fixedUpdate() {
        scheduler.doStage(Stage.FIXED_UPDATE, (sys) -> sys.fixedUpdate(this), true);
    }

    public void close() {
        scheduler.doStage(Stage.CLOSE, (sys) -> sys.close(this), false);
    }

    public void onEvent(EventDispatcher dispatch) {
        scheduler.doStage(Stage.EVENT, (sys) -> sys.onEvent(this, dispatch), true);
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
}
