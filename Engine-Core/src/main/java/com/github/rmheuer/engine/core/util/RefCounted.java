package com.github.rmheuer.engine.core.util;

public abstract class RefCounted {
    private int refCount;

    public RefCounted() {
        refCount = 0;
    }

    protected abstract void free();

    public void claim() {
        refCount++;
    }

    public void release() {
        refCount--;
        if (refCount == 0) {
            free();
        }
    }
}
