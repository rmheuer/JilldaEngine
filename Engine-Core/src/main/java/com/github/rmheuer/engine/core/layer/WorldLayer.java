package com.github.rmheuer.engine.core.layer;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.SystemRegistry;
import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.main.Game;

import java.util.Collection;
import java.util.Set;

public abstract class WorldLayer implements Layer {
    private final World world;

    public WorldLayer() {
        Set<Class<? extends GameSystem>> systems = Game.get().getAllSystems();
        Collection<Class<? extends GameSystem>> additional = getAdditionalSystems();
        if (additional != null) {
            systems.addAll(additional);
        }
        world = new World(systems);
    }

    protected abstract Collection<Class<? extends GameSystem>> getAdditionalSystems();

    @Override
    public void init() {
        world.init();
    }

    @Override
    public void update(float delta) {
        world.update(delta);
    }

    @Override
    public void fixedUpdate() {
        world.fixedUpdate();
    }

    @Override
    public void onEvent(Event event) {
        world.postImmediateEvent(event);
    }

    @Override
    public void close() {
        world.close();
    }
}
