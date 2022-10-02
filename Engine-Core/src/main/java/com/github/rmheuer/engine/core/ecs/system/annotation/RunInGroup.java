package com.github.rmheuer.engine.core.ecs.system.annotation;

import com.github.rmheuer.engine.core.ecs.system.group.SystemGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RunInGroup {
    Class<? extends SystemGroup> value();
}
