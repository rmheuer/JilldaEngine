package com.github.rmheuer.engine.gui;

public final class GuiTableFlags {
    public static final int None = 0;

    public static final int NoBorder = 1;
    public static final int NoPaddingX = 1 << 1;
    public static final int NoPaddingY = 1 << 2;

    public static final int NoPadding = NoPaddingX | NoPaddingY;

    private GuiTableFlags() {
        throw new AssertionError();
    }
}
