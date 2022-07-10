package com.github.rmheuer.engine.gui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class GuiStorage {
    private static final class StorageNode {
        private final StorageNode parent;
        private final Map<Object, StorageNode> children;
        private final Map<Object, Object> values;
        private final Set<Object> unusedKeys;

        StorageNode(StorageNode parent) {
            this.parent = parent;
            children = new HashMap<>();
            values = new HashMap<>();
            unusedKeys = new HashSet<>();
        }

        <T> T get(Object id, Supplier<T> initializer) {
            unusedKeys.remove(id);

            Object o = values.computeIfAbsent(id, (k) -> initializer.get());

            @SuppressWarnings("unchecked")
            T t = (T) o;

            return t;
        }

        void disposeUnused() {
            for (StorageNode node : children.values()) {
                node.disposeUnused();
            }

            for (Object o : unusedKeys) {
                values.remove(o);
            }
            unusedKeys.clear();
            unusedKeys.addAll(values.keySet());
        }
    }

    private final StorageNode root;
    private StorageNode current;

    public GuiStorage() {
        root = new StorageNode(null);
        current = root;
    }

    public void push(Object id) {
        current = current.children.computeIfAbsent(id, (k) -> new StorageNode(current));
    }

    public void pop() {
        current = current.parent;
    }

    public <T> T get(Object id, Supplier<T> initializer) {
        return current.get(id, initializer);
    }

    public void disposeUnused() {
        if (current != root)
            throw new IllegalStateException("Storage push/pop mismatch");
        root.disposeUnused();
    }
}
