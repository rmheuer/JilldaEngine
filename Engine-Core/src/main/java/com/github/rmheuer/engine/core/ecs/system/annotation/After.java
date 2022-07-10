package com.github.rmheuer.engine.core.ecs.system.annotation;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Afters.class)
public @interface After {
    Stage stage() default Stage.UPDATE;

    Class<? extends GameSystem> after();
}
