package com.github.rmheuer.engine.core.util;

public interface ExceptionalSupplier<T, E extends Throwable> {
    T get() throws E;
}
