package com.github.rmheuer.engine.core.resource;

import com.github.rmheuer.engine.core.util.StreamUtils;
import org.lwjgl.BufferUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public abstract class ResourceFile extends Resource {
    /**
     * The character used to separate entries within a path.
     */
    public static final char SEPARATOR = '/';

    public abstract InputStream readAsStream() throws IOException;
    public abstract OutputStream writeAsStream() throws IOException;

    public String readAsString() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        StreamUtils.copyStreams(readAsStream(), b);
        return b.toString("UTF-8");
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

    public void writeByteArray(byte[] b) throws IOException {
        OutputStream out = writeAsStream();
        out.write(b);
        out.close();
    }

    public void writeString(String str) throws IOException {
        writeByteArray(str.getBytes(StandardCharsets.UTF_8));
    }
}
