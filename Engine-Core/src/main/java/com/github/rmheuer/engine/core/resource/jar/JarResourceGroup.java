package com.github.rmheuer.engine.core.resource.jar;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.ResourceGroup;
import com.github.rmheuer.engine.core.resource.ResourceUtils;

import java.util.Objects;

public final class JarResourceGroup extends ResourceGroup {
    private final String name;
    private final String path; // Local and absolute path are always the same for JAR resources

    public JarResourceGroup(String path) {
        String[] parts = ResourceUtils.splitPath(path);
        name = parts[parts.length - 1];
        this.path = path;
    }

    @Override
    public boolean exists() {
        // Can't check for folder in JAR, so assume it exists
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getAbsolutePath() {
        return path;
    }

    @Override
    public ResourceGroup getParent() {
        String[] parts = ResourceUtils.splitPath(path);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            out.append(parts[i]).append(ResourceFile.SEPARATOR);
        }
        return new JarResourceGroup(out.toString());
    }

    private String childPath(String name) {
        return path + ResourceFile.SEPARATOR + name;
    }

    @Override
    public ResourceFile getResource(String name) {
        return new JarResourceFile(childPath(name));
    }

    @Override
    public ResourceGroup getSubgroup(String name) {
        return new JarResourceGroup(childPath(name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JarResourceGroup that = (JarResourceGroup) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
