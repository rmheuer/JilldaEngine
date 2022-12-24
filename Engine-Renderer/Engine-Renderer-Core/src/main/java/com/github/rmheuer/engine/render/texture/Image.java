package com.github.rmheuer.engine.render.texture;

import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.core.nat.NativeObject;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.util.SizeOf;
import com.github.rmheuer.engine.render.RenderBackend;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

/**
 * Represents a two-dimensional RGBA image that may
 * be stored on the GPU.
 *
 * Note: There is not currently a way to read data back
 *       from the GPU if it is modified there.
 *
 * @author rmheuer
 */
public final class Image extends Subimage implements Texture {
    public static final int RED_SHIFT   = 0;
    public static final int GREEN_SHIFT = 8;
    public static final int BLUE_SHIFT  = 16;
    public static final int ALPHA_SHIFT = 24;

    public static final int RED_MASK   = 0xFF << RED_SHIFT;
    public static final int GREEN_MASK = 0xFF << GREEN_SHIFT;
    public static final int BLUE_MASK  = 0xFF << BLUE_SHIFT;
    public static final int ALPHA_MASK = 0xFF << ALPHA_SHIFT;

    public static int encodeColor(Vector4f color) {
        int r = (int) (color.x * 255);
        int g = (int) (color.y * 255);
        int b = (int) (color.z * 255);
        int a = (int) (color.w * 255);

        return r << RED_SHIFT | g << GREEN_SHIFT | b << BLUE_SHIFT | a << ALPHA_SHIFT;
    }

    public static Vector4f decodeColor(int color) {
        byte r = (byte) ((color & RED_MASK)   >>> RED_SHIFT);
        byte g = (byte) ((color & GREEN_MASK) >>> GREEN_SHIFT);
        byte b = (byte) ((color & BLUE_MASK)  >>> BLUE_SHIFT);
        byte a = (byte) ((color & ALPHA_MASK) >>> ALPHA_SHIFT);

        return new Vector4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    public static Image decode(ResourceFile file) throws IOException {
        ByteBuffer data = file.readAsDirectByteBuffer();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            IntBuffer pChannels = stack.mallocInt(1);

            ByteBuffer pixels = stbi_load_from_memory(
                    data,
                    pWidth,
                    pHeight,
                    pChannels,
                    4
            );
            if (pixels == null) {
                throw new IOException("Failed to decode image from " + file);
            }

            int width = pWidth.get(0);
            int height = pHeight.get(0);

            int[] rgbaData = new int[width * height];
            for (int i = 0; i < rgbaData.length; i++) {
                rgbaData[i] = pixels.getInt(i * SizeOf.INT);
            }
            stbi_image_free(pixels);

            return new Image(width, height, rgbaData);
        }
    }

    private final int width;
    private final int height;
    private final int[] rgbaData;

    private TextureFilter minFilter;
    private TextureFilter magFilter;

    private boolean dataDirty;
    private boolean filtersDirty;
    private Native nat;

    private final List<Consumer<Image>> dataChangeListeners;

    public Image(int width, int height) {
        this(width, height, Colors.WHITE);
    }

    public Image(int width, int height, Vector4f fillColor) {
        this(width, height, new int[width * height]);

        int fill = encodeColor(fillColor);
        Arrays.fill(rgbaData, fill);
    }

    public Image(int width, int height, int[] rgbaData) {
        super(null, 0, 0, width, height);
        src = this;

        this.width = width;
        this.height = height;
        this.rgbaData = rgbaData;

        minFilter = TextureFilter.NEAREST;
        magFilter = TextureFilter.NEAREST;

        dataDirty = true;
        filtersDirty = true;

        dataChangeListeners = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TextureFilter getMinFilter() {
        return minFilter;
    }

    public void setMinFilter(TextureFilter minFilter) {
        this.minFilter = minFilter;
        filtersDirty = true;
    }

    public TextureFilter getMagFilter() {
        return magFilter;
    }

    public void setMagFilter(TextureFilter magFilter) {
        this.magFilter = magFilter;
        filtersDirty = true;
    }

    public Vector4f getPixel(Vector2i pos) {
        return getPixel(pos.x, pos.y);
    }

    public Vector4f getPixel(int x, int y) {
        checkBounds(x, y);
        return decodeColor(rgbaData[x + y * width]);
    }

    public void setPixel(Vector2i pos, Vector4f color) {
        setPixel(pos.x, pos.y, color);
    }

    public void setPixel(int x, int y, Vector4f color) {
        checkBounds(x, y);
        rgbaData[x + y * width] = encodeColor(color);
        markDataDirty();
    }

    public void blit(Image img, int x, int y) {
        checkBounds(x, y);
        if (x + img.width > width || y + img.height > height)
            throw new IndexOutOfBoundsException("Image extends out of bounds");

        for (int row = 0; row < img.height; row++) {
            System.arraycopy(img.rgbaData, row * img.width, rgbaData, x + (y + row) * width, img.width);
        }

        markDataDirty();
    }

    private void checkBounds(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException(x + ", " + y);
        }
    }

    public int[] getRgbaData() {
        return rgbaData;
    }

    /**
     * Marks the RGBA pixel data as dirty. This must
     * be called after modifying the data returned from
     * {@link #getRgbaData()} in order to see the result
     * of the change.
     */
    public void markDataDirty() {
        dataDirty = true;
        for (Consumer<Image> listener : dataChangeListeners) {
            listener.accept(this);
        }
    }

    public void addDataChangeListener(Consumer<Image> listener) {
        dataChangeListeners.add(listener);
    }

    public void removeDataChangeListener(Consumer<Image> listener) {
        dataChangeListeners.remove(listener);
    }

    public interface Native extends Texture.Native {
        void setData(int[] rgbaData);
        void setFilters(TextureFilter minFilter, TextureFilter magFilter);
    }

    @Override
    public Native getNative(NativeObjectManager mgr) {
        if (nat == null) {
            nat = RenderBackend.get().createImageNative(width, height);
            mgr.registerObject(nat);
        }

        if (dataDirty) {
            dataDirty = false;
            nat.setData(rgbaData);
        }

        if (filtersDirty) {
            filtersDirty = false;
            nat.setFilters(minFilter, magFilter);
        }

        return nat;
    }
}
