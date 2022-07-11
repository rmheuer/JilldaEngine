package com.github.rmheuer.engine.core.resource;

import com.github.rmheuer.engine.core.util.StreamUtils;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Scanner;

public abstract class ResourceFile implements Resource {
    /**
     * The character used to separate entries within a path.
     */
    public static final char SEPARATOR = '/';

    public abstract boolean exists();

    public abstract InputStream readAsStream() throws IOException;
    public abstract OutputStream writeAsStream() throws IOException;

    public String readAsString() throws IOException {
        return new Scanner(readAsStream(), "UTF-8")
                .useDelimiter("\\A")
                .next();
    }

    public byte[] readAsByteArray() throws IOException {
        return StreamUtils.readToByteArray(readAsStream());
    }

    public ByteBuffer readAsDirectByteBuffer() throws IOException {
        byte[] data = readAsByteArray();
        ByteBuffer buf = BufferUtils.createByteBuffer(data.length);
        buf.put(data);
        buf.flip();
        return buf;
    }

    @Override
    public boolean isFile() {
	return true;
    }

    @Override
    public boolean isGroup() {
	return false;
    }
}
