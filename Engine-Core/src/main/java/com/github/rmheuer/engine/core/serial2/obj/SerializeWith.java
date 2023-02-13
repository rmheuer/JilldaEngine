package com.github.rmheuer.engine.core.serial2.obj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface SerializeWith {
    Class<? extends SerialMapper<?, ?>> value();
}
