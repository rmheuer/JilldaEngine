package com.github.rmheuer.engine.gui;

public final class GuiWindowFlags {
    public static final int None = 0;

    public static final int NoTitleBar = 1;
    public static final int NoBorder = 1 << 1;
    public static final int NoBackground = 1 << 2;
    public static final int NoDecorations = NoTitleBar | NoBorder | NoBackground;

    public static final int NoMove = 1 << 3;
    public static final int NoResize = 1 << 4;
    public static final int NoScrollX = 1 << 5;
    public static final int NoScrollY = 1 << 6;
    public static final int NoScroll = NoScrollX | NoScrollY;
    public static final int NoInteraction = NoMove | NoResize | NoScroll;

    private GuiWindowFlags() {
        throw new AssertionError();
    }
}
