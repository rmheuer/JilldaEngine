package com.github.rmheuer.engine.core.nat;

/**
 * Represents an object that has associated native data
 * that needs to be freed when the object is no longer
 * in use.
 *
 * @author rmheuer
 */
public interface NativeObject {
    /**
     * Gets a function to be called to free the native object.
     *
     * IMPORTANT:
     *     This function cannot hold a reference to the instance
     *     of this object! This will prevent it from being
     *     automatically freed.
     *
     * @return free function
     */
    NativeObjectFreeFn getFreeFn();
}
