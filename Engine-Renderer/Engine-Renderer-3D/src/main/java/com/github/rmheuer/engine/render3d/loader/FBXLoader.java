package com.github.rmheuer.engine.render3d.loader;

import com.github.rmheuer.engine.core.resource.ResourceFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class FBXLoader {
    private static final String MAGIC = "Kaydara FBX Binary";

    public static void loadFbx(ResourceFile file) throws IOException {
        new FBXLoader(file).load();
    }

    private static final class Node {
        String name;
        List<Object> properties;
        List<Node> children;

        public Node(String name) {
            this.name = name;
            properties = new ArrayList<>();
            children = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", properties=" + properties +
                    ", children=" + children +
                    '}';
        }
    }

    private final byte[] data;
    private long cursor;
    private boolean is64Bit;

    private FBXLoader(ResourceFile file) throws IOException {
        data = file.readAsByteArray();
    }

    private int read() throws IOException {
        return data[(int) (cursor++)];
    }

    private char readChar8() throws IOException {
        return (char) read();
    }

    private short readUint8() throws IOException {
        return (short) read();
    }

    private long readUint32() throws IOException {
        long l = 0;
        for (int i = 0; i < 4; i++)
            l |= read() << (i * 8);
        return l;
    }

    private long readUint64() throws IOException {
        long l = 0;
        for (int i = 0; i < 8; i++)
            l |= read() << (i * 8);
        return l;
    }

    private boolean readBool() throws IOException {
        return read() != 0;
    }

    private byte readInt8() throws IOException {
        return (byte) read();
    }

    private short readInt16() throws IOException {
        return (short) ((readInt8() & 0xFF) | (readInt8() << 8));
    }

    private int readInt32() throws IOException {
        return (read() & 0xFF)
                | ((read() & 0xFF) << 8)
                | ((read() & 0xFF) << 16)
                | ((read() & 0xFF) << 24);
    }

    private long readInt64() throws IOException {
        long out = 0;
        for (int i = 0; i < 8; i++) {
            out |= ((read() * 0xFF) << (8 * i));
        }
        return out;
    }

    private float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt32());
    }

    private double readDouble() throws IOException {
        return Double.longBitsToDouble(readInt64());
    }

    private byte[] readBytes(int len) throws IOException {
        byte[] buf = new byte[len];
        for (int i = 0; i < len; i++)
            buf[i] = readInt8();
        return buf;
    }

    private String readAscii(int len) throws IOException {
        char[] data = new char[len];
        for (int i = 0; i < len; i++) {
            data[i] = readChar8();
        }
        return new String(data);
    }

    private void readHeader() throws IOException {
        cursor = 0;

        for (char c : MAGIC.toCharArray()) {
            char in = readChar8();
            if (in != c)
                throw new IOException("Invalid FBX binary file");
        }

        // Skip ' ', ' ', 0x00, 0x1A, 0x00
        for (int i = 0; i < 5; i++)
            read();

        long version = readUint32();
        is64Bit = version >= 7500; // FBX versions past 7.5 use 64-bit node properties
    }

    private Object readArrayProperty(char type) throws IOException {
        long length = readUint32();
        long encoding = readUint32();
        long compressedLength = readUint32();

        byte[] data;
        if (encoding == 0) {
            data = readBytes()
        }
    }

    private Object readProperty() throws IOException {
        char type = readChar8();
        switch (type) {
            case 'Y': return readInt16();
            case 'C': return readBool();
            case 'I': return readInt32();
            case 'F': return readFloat();
            case 'D': return readDouble();
            case 'L': return readInt64();
            default:
                return readArrayProperty(type);
        }
    }

    private Node readNode() throws IOException {
        long endOffset = is64Bit ? readUint64() : readUint32();
        long propertyCount = is64Bit ? readUint64() : readUint32();
        long propertyListLen = is64Bit ? readUint64() : readUint32();
        short nameLen = readUint8();
        String name = readAscii(nameLen);
        Node node = new Node(name);
        for (int i = 0; i < propertyListLen; i++) {
            node.properties.add(readProperty());
        }

        // TODO: Children

        return node;
    }

    private void load() throws IOException {
        readHeader();

        Node rootNode = readNode();
        System.out.println(rootNode);
    }
}
