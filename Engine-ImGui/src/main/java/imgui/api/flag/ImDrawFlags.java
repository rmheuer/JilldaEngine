package imgui.api.flag;

public final class ImDrawFlags {
    public static final int None = 0;
    public static final int Closed = 1;
    public static final int RoundCornersTopLeft = 1 << 4;
    public static final int RoundCornersTopRight = 1 << 5;
    public static final int RoundCornersBottomLeft = 1 << 6;
    public static final int RoundCornersBottomRight = 1 << 7;
    public static final int RoundCornersNone = 1 << 8;
    public static final int RoundCornersTop = RoundCornersTopLeft | RoundCornersTopRight;
    public static final int RoundCornersBottom = RoundCornersBottomLeft | RoundCornersBottomRight;
    public static final int RoundCornersLeft = RoundCornersBottomLeft | RoundCornersTopLeft;
    public static final int RoundCornersRight = RoundCornersBottomRight | RoundCornersTopRight;
    public static final int RoundCornersAll = RoundCornersTopLeft | RoundCornersTopRight | RoundCornersBottomLeft | RoundCornersBottomRight;
    public static final int RoundCornersDefault_ = RoundCornersAll;
    public static final int RoundCornersMask_ = RoundCornersAll | RoundCornersNone;

    private ImDrawFlags() {
        throw new AssertionError();
    }
}
