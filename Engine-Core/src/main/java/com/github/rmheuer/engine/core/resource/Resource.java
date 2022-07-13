package com.github.rmheuer.engine.core.resource;

import java.io.IOException;
import java.util.Objects;

public abstract class Resource {
    public abstract String getName();
    public abstract String getPath();
    public abstract String getAbsolutePath();

    public abstract boolean exists();

    public abstract ResourceGroup getParent();

    public Resource getSibling(String name) {
        ResourceGroup parent = getParent();
        if (parent == null)
            return null;

        return parent.getChild(name);
    }

    public void rename(String newName) throws IOException {
        throw new IOException("Renaming is not supported");
    }

    public void create() throws IOException {
        throw new IOException("Creating is not supported");
    }

    public void delete() {
        throw new UnsupportedOperationException("Deleting is not supported");
    }

    public boolean isFile() {
        return this instanceof ResourceFile;
    }

    public boolean isGroup() {
        return this instanceof ResourceGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!o.getClass().equals(getClass())) return false;

        Resource r = (Resource) o;
        return getAbsolutePath().equals(r.getAbsolutePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAbsolutePath());
    }
}
