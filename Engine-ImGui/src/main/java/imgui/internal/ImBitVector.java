package imgui.internal;

import java.util.Arrays;

import static imgui.api.ImGuiMacros.IM_ASSERT;
import static imgui.internal.ImGuiInternal.*;

public final class ImBitVector {
    private int[] storage;

    public void create(int sz) {
        storage = new int[sz];
    }

    public void clear() {
        Arrays.fill(storage, 0);
    }

    public boolean testBit(int n) {
        IM_ASSERT(n < (storage.length << 5));
        return imBitArrayTestBit(storage, n);
    }

    public void setBit(int n) {
        IM_ASSERT(n < (storage.length << 5));
        imBitArraySetBit(storage, n);
    }

    public void clearBit(int n) {
        IM_ASSERT(n < (storage.length << 5));
        imBitArrayClearBit(storage, n);
    }
}
