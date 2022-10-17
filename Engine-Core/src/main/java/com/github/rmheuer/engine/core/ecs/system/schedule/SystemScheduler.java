package com.github.rmheuer.engine.core.ecs.system.schedule;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.schedule.info.RepeatableNodeOrderingInfo;
import com.github.rmheuer.engine.core.ecs.system.schedule.info.SystemInfo;
import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.util.LazyCache;
import com.github.rmheuer.engine.core.util.Pair;

import java.util.Map;
import java.util.Set;

public final class SystemScheduler {
    private final StageScheduler<GameSystem> init, update, fixedUpdate, close;
    private final LazyCache<Class<?>, StageScheduler<RepeatableNodeOrderingInfo>> event, componentAdd, componentRemove;

    public SystemScheduler() {
        init = new StageScheduler<>();
        update = new StageScheduler<>();
        fixedUpdate = new StageScheduler<>();
        close = new StageScheduler<>();

        event = new LazyCache<>((type) -> new StageScheduler<>());
        componentAdd = new LazyCache<>((type) -> new StageScheduler<>());
        componentRemove = new LazyCache<>((type) -> new StageScheduler<>());
    }

    public void setSystems(Set<Class<? extends GameSystem>> systems) {
        for (Class<? extends GameSystem> system : systems) {
            SystemInfo info = SystemInfo.get(system);

            GameSystem inst = info.getInstance();
            init.add(inst, info.getInit());
            update.add(inst, info.getUpdate());
            fixedUpdate.add(inst, info.getFixedUpdate());
            close.add(inst, info.getClose());

            for (Pair<Class<?>, RepeatableNodeOrderingInfo> pair : info.getEvent())
                event.get(pair.getA()).add(pair.getB(), pair.getB());
            for (Pair<Class<?>, RepeatableNodeOrderingInfo> pair : info.getComponentAdd())
                componentAdd.get(pair.getA()).add(pair.getB(), pair.getB());
            for (Pair<Class<?>, RepeatableNodeOrderingInfo> pair : info.getComponentRemove())
                componentRemove.get(pair.getA()).add(pair.getB(), pair.getB());
        }

        dump();
    }

    private void dump(String name, StageScheduler<?> stage) {
        System.out.println(name + ":");
        stage.dump();
    }

    public void dump() {
        System.out.println("-------- Static --------");
        dump("Init", init);
        dump("Update", update);
        dump("Fixed update", fixedUpdate);
        dump("Close", close);
        System.out.println("-------- Event --------");
        for (Map.Entry<Class<?>, StageScheduler<RepeatableNodeOrderingInfo>> entry : event.entrySet()) {
            dump(entry.getKey().getSimpleName(), entry.getValue());
        }
        System.out.println("-------- Comp. Add --------");
        for (Map.Entry<Class<?>, StageScheduler<RepeatableNodeOrderingInfo>> entry : componentAdd.entrySet()) {
            dump(entry.getKey().getSimpleName(), entry.getValue());
        }
        System.out.println("-------- Comp. Rmv --------");
        for (Map.Entry<Class<?>, StageScheduler<RepeatableNodeOrderingInfo>> entry : componentRemove.entrySet()) {
            dump(entry.getKey().getSimpleName(), entry.getValue());
        }
    }

    public void init(World world) {
        init.forEach((sys) -> {
            sys.init(world);
        });
    }

    public void update(World world, float delta) {
        update.forEach((sys) -> {
            sys.update(world, delta);
        });
    }

    public void fixedUpdate(World world) {
        fixedUpdate.forEach((sys) -> {
            sys.fixedUpdate(world);
        });
    }

    public void close(World world) {
        close.forEach((sys) -> {
            sys.close(world);
        });
    }

    public void onEvent(World world, Event event) {
        Class<?> type = event.getClass();
        do {
            StageScheduler<RepeatableNodeOrderingInfo> stage = this.event.get(type);
            if (stage == null)
                return;
            stage.forEach((info) -> {
                info.invoke(world, event);
            });

            // Repeat with the next class up for superclass handlers
            type = type.getSuperclass();
        } while (Event.class.isAssignableFrom(type));

        StageScheduler<RepeatableNodeOrderingInfo> rawStage = this.event.get(Event.class);
        if (rawStage == null)
            return;
        rawStage.forEach((info) -> {
            info.invoke(world, event);
        });
    }

    public void onComponentAdd(World world, Component c) {
        StageScheduler<RepeatableNodeOrderingInfo> stage = componentAdd.get(c.getClass());
        if (stage == null)
            return;
        stage.forEach((info) -> {
            info.invoke(world, c);
        });
    }

    public void onComponentRemove(World world, Component c) {
        StageScheduler<RepeatableNodeOrderingInfo> stage = componentRemove.get(c.getClass());
        if (stage == null)
            return;
        stage.forEach((info) -> {
            info.invoke(world, c);
        });
    }
}
