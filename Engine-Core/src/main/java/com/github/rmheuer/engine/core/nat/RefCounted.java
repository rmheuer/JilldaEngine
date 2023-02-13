package com.github.rmheuer.engine.core.nat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RefCounted {
    private final AtomicInteger refCount;
    private final List<Runnable> freeListeners;

    public RefCounted() {
        refCount = new AtomicInteger(0);
        freeListeners = new ArrayList<>();
    }

    protected abstract void free();

    public void retain() {
        if (refCount.getAndIncrement() <= 0)
            throw new IllegalStateException("Reference-counted object already freed");
    }

    public void release() {
        int remaining = refCount.decrementAndGet();
        if (remaining == 0) {
            free();
            for (Runnable fn : freeListeners)
                fn.run();
        }
        if (remaining < 0)
            throw new IllegalStateException("Reference-counted object already freed");
    }

    public int getReferenceCount() {
        return refCount.get();
    }

    public void addFreeListener(Runnable listener) {
        freeListeners.add(listener);
    }
}
