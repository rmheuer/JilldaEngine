package com.github.rmheuer.engine.render.opengl;

import com.github.rmheuer.engine.core.nat.NativeObjectFreeFn;

import java.util.function.IntConsumer;

/**
 * Allows passing the OpenGL integer ID of an object into its free
 * function without referencing the native object.
 *
 * @author rmheuer
 */
public final class GLDestructor implements NativeObjectFreeFn {
    private final int id;
    private final IntConsumer deleteFn;

    public GLDestructor(int id, IntConsumer deleteFn) {
        this.id = id;
        this.deleteFn = deleteFn;
    }

    @Override
    public void free() {
        deleteFn.accept(id);
    }
}
