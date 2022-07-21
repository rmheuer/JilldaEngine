package com.github.rmheuer.engine.core.resource.jar;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.ResourceGroup;
import com.github.rmheuer.engine.core.resource.ResourceUtils;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.node.SerialString;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializeWith;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

@SerializeWith(JarResourceFile.Serializer.class)
public final class JarResourceFile extends ResourceFile {
    public static final class Serializer implements TypeCodec<JarResourceFile> {
        @Override
        public SerialNode serialize(JarResourceFile jarResourceFile, SerializationContext ctx) {
            return new SerialString(jarResourceFile.getPath());
        }

        @Override
        public JarResourceFile deserialize(SerialNode node, DeserializationContext ctx) {
            return new JarResourceFile(((SerialString) node).getValue());
        }
    }

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

    @Override
    public ResourceGroup getParent() {
        String[] parts = ResourceUtils.splitPath(path);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            out.append(parts[i]).append(ResourceFile.SEPARATOR);
        }
        return new JarResourceGroup(out.toString());
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
