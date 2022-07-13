package com.github.rmheuer.engine.gui;

public final class GuiTextEditFlags {
    public static final int None = 0;

    public static final int ReturnFocused = 1;
    public static final int AlignToTreeSize = 1 << 1;

    // Actions
    public static final int Focus = 1 << 2;
    public static final int SelectAll = 1 << 3;

    private GuiTextEditFlags() {
        throw new AssertionError();
    }
}
