package com.github.rmheuer.engine.core.resource.jar;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public final class JarResourceFile extends ResourceFile {
    private final String name;
    private final String path;

    public JarResourceFile(String path) {
        String[] parts = ResourceUtils.splitPath(path);
        name = parts[parts.length - 1];
        this.path = path;
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

    private InputStream getStream() {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    @Override
    public boolean exists() {
        return getStream() != null;
    }

    @Override
    public InputStream readAsStream() throws IOException {
        InputStream in = getStream();
        if (in == null) {
            throw new IOException("Resource not found: " + path);
        }
        return in;
    }

    @Override
    public OutputStream writeAsStream() throws IOException {
        throw new IOException("JAR resources are not writable");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JarResourceFile that = (JarResourceFile) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
