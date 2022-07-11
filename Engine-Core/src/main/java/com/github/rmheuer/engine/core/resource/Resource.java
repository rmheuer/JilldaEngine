package com.github.rmheuer.engine.core.resource;

import java.util.Objects;

public abstract class Resource {
    public abstract String getName();
    public abstract String getPath();
    public abstract String getAbsolutePath();

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
