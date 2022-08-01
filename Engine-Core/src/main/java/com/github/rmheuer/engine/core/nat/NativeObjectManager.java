package com.github.rmheuer.engine.core.nat;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.WeakHashMap;

/**
 * A class to manage the lifetime of {@code NativeObject}s.
 *
 * @see NativeObject
 * @author rmheuer
 */
public final class NativeObjectManager {
    private static final class ObjectRef extends WeakReference<NativeObject> {
        private final NativeObjectFreeFn freeFn;

        // For debug logging
        final Class<?> clazz;

        public ObjectRef(ReferenceQueue<Object> refQueue, NativeObject obj) {
            super(obj, refQueue);
            freeFn = obj.getFreeFn();
            clazz = obj.getClass();
        }

        public void free() {
            clear();
            freeFn.free();
        }
    }

    private final ReferenceQueue<Object> refQueue;
    private final Map<NativeObject, ObjectRef> activeReferences;
    private final Queue<NativeObject> requestedFrees;

    public NativeObjectManager() {
        refQueue = new ReferenceQueue<>();
        activeReferences = new WeakHashMap<>();
        requestedFrees = new ArrayDeque<>();
    }

    /**
     * Registers an object with this NativeObjectManager
     * to manage its lifetime.
     *
     * @param obj object to register
     */
    public void registerObject(NativeObject obj) {
        ObjectRef ref = new ObjectRef(refQueue, obj);
        activeReferences.put(obj, ref);
        System.out.println("[Debug] Registered native object of " + ref.clazz);
    }

    /**
     * Explicitly frees a native object.
     *
     * @param obj object to free
     */
    public void freeObject(NativeObject obj) {
        requestedFrees.add(obj);
    }

    /**
     * Frees all objects that are no longer referenced,
     * up to a given limit. If the limit is less than zero,
     * no limit will be applied.
     *
     * @param limit max objects to free
     */
    public void freeUnusedObjects(int limit) {
        boolean overrideLimit = limit < 0;

        int removed = 0;
        while ((overrideLimit || removed < limit) && !requestedFrees.isEmpty()) {
            NativeObject obj = requestedFrees.remove();
            freeObjectInternal(obj);
            removed++;
            System.out.println("[Debug] Explicitly freed native object of " + obj.getClass());
        }
        while (overrideLimit || removed < limit) {
            ObjectRef ref = (ObjectRef) refQueue.poll();
            if (ref == null)
                break;

            ref.free();
            removed++;

            System.out.println("[Debug] Dynamically freed native object of " + ref.clazz);

            // Don't need to remove object from activeReferences because
            // it is no longer referenced and activeReferences is weak
        }
    }

    /**
     * Frees all remaining native objects.
     */
    public void freeAllObjects() {
        freeUnusedObjects(-1);
        for (ObjectRef ref : activeReferences.values()) {
            System.out.println("[Debug] Bulk freed native object of " + ref.clazz);
            ref.free();
        }
        activeReferences.clear();
    }

    private void freeObjectInternal(NativeObject obj) {
        ObjectRef ref = activeReferences.remove(obj);
        if (ref != null) {
            ref.free();
        } else {
            obj.getFreeFn().free();
        }
    }
}
