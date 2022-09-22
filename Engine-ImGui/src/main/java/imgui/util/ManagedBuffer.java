package imgui.util;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public final class ManagedBuffer {
    private final ByteBuffer buffer;
    private final Consumer<ByteBuffer> freeFn;

    public ManagedBuffer(ByteBuffer buffer, Consumer<ByteBuffer> freeFn) {
        this.buffer = buffer;
        this.freeFn = freeFn;
    }

    public void free() {
        freeFn.accept(buffer);
    }
}
