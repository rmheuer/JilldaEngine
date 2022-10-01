package imgui.internal;

import java.util.Arrays;

import static imgui.api.ImGuiMacros.IM_ASSERT;
import static imgui.internal.ImGuiInternal.*;

public final class ImBitArray {
    private final int bitCount, offset;
    private final int[] storage;

    public ImBitArray(int bitCount, int offset) {
        this.bitCount = bitCount;
        this.offset = offset;
        storage = new int[(bitCount + 31) >> 5];

        clearAllBits();
    }

    public void clearAllBits() {
        Arrays.fill(storage, 0);
    }

    public void setAllBits() {
        Arrays.fill(storage, 0xFFFFFFFF);
    }

    public boolean testBit(int n) {
        n += offset;
        IM_ASSERT(n >= 0 && n < bitCount);
        return imBitArrayTestBit(storage, n);
    }

    public void setBit(int n) {
        n += offset;
        IM_ASSERT(n >= 0 && n < bitCount);
        imBitArraySetBit(storage, n);
    }

    public void clearBit(int n) {
        n += offset;
        IM_ASSERT(n >= 0 && n < bitCount);
        imBitArrayClearBit(storage, n);
    }

    public void setBitRange(int n, int n2) {
        n += offset;
        n2 += offset;
        IM_ASSERT(n >= 0 && n < bitCount && n2 > n && n2 <= bitCount);
        imBitArraySetBitRange(storage, n, n2);
    }
}
