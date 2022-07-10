package com.github.rmheuer.engine.core.ecs.system.annotation;

import com.github.rmheuer.engine.core.ecs.system.SystemGroup;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RunInGroup {
    Stage stage() default Stage.UPDATE;

    Class<? extends SystemGroup> group();
}
