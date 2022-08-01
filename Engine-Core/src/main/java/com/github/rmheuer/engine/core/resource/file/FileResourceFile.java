package com.github.rmheuer.engine.core.resource.file;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.ResourceGroup;
import com.github.rmheuer.engine.core.resource.ResourceUtils;
import com.github.rmheuer.engine.core.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

// TODO: Cache path and absolute path
public final class FileResourceFile extends ResourceFile {
    private final File file;

    public FileResourceFile(String filesystemPath) {
        this(new File(filesystemPath));
    }

    public FileResourceFile(File file) {
        if (file.exists() && !file.isFile())
            throw new IllegalArgumentException("Not a file: " + file);
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
    public ResourceGroup getParent() {
        return new FileResourceGroup(file.getParentFile());
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public void rename(String name) throws IOException {
        // Don't try to rename to current name
        if (name.equals(file.getName()))
            return;

        File dest = new File(file.getParentFile(), name);
        StreamUtils.copyStreams(readAsStream(), new FileOutputStream(dest));
        file.delete();
    }

    @Override
    public void create() throws IOException {
        boolean mkdirOut = file.getParentFile().mkdirs();
        boolean createOut = file.createNewFile();

        if (!mkdirOut || !createOut) {
            throw new IOException("Failed to create new file");
        }
    }

    @Override
    public void delete() {
        file.delete();
    }

    @Override
    public InputStream readAsStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public OutputStream writeAsStream() throws IOException {
        return new FileOutputStream(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileResourceFile that = (FileResourceFile) o;
        return Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
