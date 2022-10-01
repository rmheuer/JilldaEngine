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
        Set<GameSystem> systems = Game.get().getAllSystems();
        Collection<Class<? extends GameSystem>> additional = getAdditionalSystems();
        if (additional != null) {
            for (Class<? extends GameSystem> clazz : additional) {
                systems.add(SystemRegistry.getInstance(clazz));
            }
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
        world.onEvent(new EventDispatcher(event));
    }

    @Override
    public void close() {
        world.close();
    }
}
