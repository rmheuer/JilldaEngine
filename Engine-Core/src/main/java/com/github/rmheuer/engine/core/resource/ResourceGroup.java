package com.github.rmheuer.engine.core.resource;

import java.util.Set;

public abstract class ResourceGroup extends Resource {
    public abstract ResourceFile getResource(String name);
    public abstract ResourceGroup getSubgroup(String name);

    /**
     * Gets whether it is safe to use {@link #getResources} and {@link #getSubgroups()}.
     *
     * @return if children are iterable
     */
    public boolean canIterateChildren() {
        return false;
    }

    /**
     * Gets a set of all {@code Resource}s that are a direct descendant of this
     * resource group. This is an optional operation, and will throw
     * {@code UnsupportedOperationException} if it is not implemented. This
     * method should not throw an {@code UnsupportedOperationException} if
     * {@link #canIterateChildren()} returns {@code true}.
     *
     * @return contained resources
     * @throws UnsupportedOperationException if not implemented
     */
    public Set<ResourceFile> getResources() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets a set of all {@code ResourceGroup}s that are a direct descendant of
     * this resource group. This is an optional operation, and will throw
     * {@code UnsupportedOperationException} if it is not implemented. This
     * method should not throw an {@code UnsupportedOperationException} if
     * {@link #canIterateChildren()} returns {@code true}.
     *
     * @return contained resources
     * @throws UnsupportedOperationException if not implemented
     */
    public Set<ResourceGroup> getSubgroups() {
        throw new UnsupportedOperationException();
    }
}
