package com.github.rmheuer.engine.render.texture;

import com.github.rmheuer.engine.core.asset.Asset;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.io.IOException;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public final class TextureData extends Asset {
    public static TextureData decode(ResourceFile res) throws IOException {
        ByteBuffer data = res.readAsDirectByteBuffer();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer pixels = stbi_load_from_memory(
                    data,
                    width,
                    height,
                    channels,
                    4
            );

            if (pixels == null) {
                throw new IOException("Failed to decode image: " + res);
            }

            return new TextureData(
                    pixels,
                    width.get(0),
                    height.get(0),
                    () -> stbi_image_free(pixels)
            );
        }
    }

    public static TextureData fromByteArray(byte[] pixels, int width, int height) {
        ByteBuffer buf = BufferUtils.createByteBuffer(pixels.length);
        buf.put(pixels);
        buf.flip();

        return new TextureData(buf, width, height, () -> {
        });
    }

    private final ByteBuffer pixels;
    private final int width;
    private final int height;
    private final Runnable freeFn;

    public TextureData(ByteBuffer pixels, int width, int height, Runnable freeFn) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.freeFn = freeFn;
    }

    public ByteBuffer getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void freeAsset() {
        freeFn.run();
    }
}
