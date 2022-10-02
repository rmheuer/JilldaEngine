package com.github.rmheuer.engine.core.ecs.system.schedule.info;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnComponentAdd;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnComponentRemove;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.util.LazyCache;
import com.github.rmheuer.engine.core.util.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class SystemInfo {
    private static final LazyCache<Class<? extends GameSystem>, SystemInfo> CACHE = new LazyCache<>(SystemInfo::new);
    public static SystemInfo get(Class<? extends GameSystem> type) {
        return CACHE.get(type);
    }

    private final GameSystem instance;
    private final NodeOrderingInfo init, update, fixedUpdate, close;
    private final List<Pair<Class<?>, RepeatableNodeOrderingInfo>> event, componentAdd, componentRemove;

    private NodeOrderingInfo getNodeInfo(Class<? extends GameSystem> type, String name, Class<?>... paramTypes) throws ReflectiveOperationException {
        Method m = type.getMethod(name, paramTypes);
        if (!m.getDeclaringClass().equals(type))
            return null;
        return new NodeOrderingInfo(type, m, false);
    }

    private List<Pair<Class<?>, RepeatableNodeOrderingInfo>> getRepeatableNodeInfo(GameSystem inst, Class<? extends Annotation> marker, Class<?> requiredBaseClass) {
        List<Pair<Class<?>, RepeatableNodeOrderingInfo>> out = new ArrayList<>();
        for (Method m : inst.getClass().getMethods()) {
            if (!m.isAnnotationPresent(marker))
                continue;

            RepeatableNodeOrderingInfo info = new RepeatableNodeOrderingInfo(inst, m, requiredBaseClass);
            out.add(new Pair<>(info.getParamType(), info));
        }
        return out;
    }

    private SystemInfo(Class<? extends GameSystem> type) {
        try {
            instance = type.getConstructor().newInstance();

            init = getNodeInfo(type, "init", World.class);
            update = getNodeInfo(type, "update", World.class, float.class);
            fixedUpdate = getNodeInfo(type, "fixedUpdate", World.class);
            close = getNodeInfo(type, "close", World.class);

            event = getRepeatableNodeInfo(instance, OnEvent.class, Event.class);
            componentAdd = getRepeatableNodeInfo(instance, OnComponentAdd.class, Component.class);
            componentRemove = getRepeatableNodeInfo(instance, OnComponentRemove.class, Component.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to get system info for " + type);
        }
    }

    public GameSystem getInstance() {
        return instance;
    }

    public NodeOrderingInfo getInit() {
        return init;
    }

    public NodeOrderingInfo getUpdate() {
        return update;
    }

    public NodeOrderingInfo getFixedUpdate() {
        return fixedUpdate;
    }

    public NodeOrderingInfo getClose() {
        return close;
    }

    public List<Pair<Class<?>, RepeatableNodeOrderingInfo>> getEvent() {
        return event;
    }

    public List<Pair<Class<?>, RepeatableNodeOrderingInfo>> getComponentAdd() {
        return componentAdd;
    }

    public List<Pair<Class<?>, RepeatableNodeOrderingInfo>> getComponentRemove() {
        return componentRemove;
    }
}
