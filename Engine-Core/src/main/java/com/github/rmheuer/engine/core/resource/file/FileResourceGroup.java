package com.github.rmheuer.engine.core.resource.file;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.ResourceGroup;
import com.github.rmheuer.engine.core.resource.ResourceUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// TODO: Cache path and absolute path
public final class FileResourceGroup extends ResourceGroup {
    private final File file;

    public FileResourceGroup(String filesystemPath) {
        this(new File(filesystemPath));
    }

    public FileResourceGroup(File file) {
        if (!file.isDirectory())
            throw new IllegalArgumentException("Not a directory: " + file);
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getPath() {
        return ResourceUtils.convertNativePathToResourcePath(file.getPath());
    }

    @Override
    public String getAbsolutePath() {
        return ResourceUtils.convertNativePathToResourcePath(file.getAbsolutePath());
    }

    @Override
    public ResourceFile getResource(String name) {
        return new FileResourceFile(new File(file, name));
    }

    @Override
    public ResourceGroup getSubgroup(String name) {
        return new FileResourceGroup(new File(file, name));
    }

    @Override
    public boolean canIterateChildren() {
        return true;
    }

    @Override
    public Set<ResourceFile> getResources() {
        File[] children = file.listFiles();
        if (children == null)
            throw new IllegalStateException("No children");

        Set<ResourceFile> out = new HashSet<>();
        for (File child : children) {
            if (child.isFile()) {
                out.add(new FileResourceFile(child));
            }
        }

        return out;
    }

    @Override
    public Set<ResourceGroup> getSubgroups() {
        File[] children = file.listFiles();
        if (children == null)
            throw new IllegalStateException("No children");

        Set<ResourceGroup> out = new HashSet<>();
        for (File child : children) {
            if (child.isDirectory()) {
                out.add(new FileResourceGroup(child));
            }
        }

        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileResourceGroup that = (FileResourceGroup) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
