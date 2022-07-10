package com.github.rmheuer.engine.core.util;

@FunctionalInterface
public interface QuadConsumer<A, B, C, D> {
    void accept(A a, B b, C c, D d);
}
