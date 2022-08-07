package com.github.rmheuer.engine.core.util;

public final class ExceptionalPeekableStream<T, E extends Throwable> {
    private final ExceptionalSupplier<T, E> supplier;
    private T lookahead;

    public ExceptionalPeekableStream(ExceptionalSupplier<T, E> supplier) {
        this.supplier = supplier;
        lookahead = null;
    }

    public T next() throws E {
        if (lookahead != null) {
            T temp = lookahead;
            lookahead = null;
            return temp;
        }

        return supplier.get();
    }

    public T peek() throws E {
        if (lookahead != null)
            return lookahead;

        lookahead = supplier.get();
        return lookahead;
    }
}
