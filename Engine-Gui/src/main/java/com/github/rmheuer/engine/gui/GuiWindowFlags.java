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

    public static final int AutoSizeX = 1 << 7;
    public static final int AutoSizeY = 1 << 8;

    static final int _IsPopup = 1 << 31;
    static final int _Popup = _IsPopup | NoTitleBar | AutoSizeX | AutoSizeY | NoInteraction;

    private GuiWindowFlags() {
        throw new AssertionError();
    }
}
