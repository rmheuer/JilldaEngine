package com.github.rmheuer.engine.core.ecs.system.schedule.info;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;

import java.lang.reflect.Method;

public final class RepeatableNodeOrderingInfo extends NodeOrderingInfo {
    private final GameSystem inst;
    private final Class<?> paramType;
    private final Method method;

    public RepeatableNodeOrderingInfo(GameSystem inst, Method m, Class<?> requiredBaseClass) {
        super(inst.getClass(), m, false);
        this.inst = inst;

        Class<?>[] params = m.getParameterTypes();
        if (params.length != 2)
            throw new RuntimeException("Handler does not have 2 parameters: " + m);
        if (!params[0].equals(World.class))
            throw new RuntimeException("Handler's first parameter is not World: " + m);
        if (!requiredBaseClass.isAssignableFrom(params[1]))
            throw new RuntimeException("Handler's second parameter is not subclass of " + requiredBaseClass.getSimpleName() + ": " + m);

        method = m;
        paramType = params[1];
    }

    public void invoke(World world, Object param) {
        try {
            method.invoke(inst, world, param);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to invoke handler " + method + " with parameter " + param, e);
        }
    }

    public GameSystem getInstance() {
        return inst;
    }

    public Class<?> getParamType() {
        return paramType;
    }
}
