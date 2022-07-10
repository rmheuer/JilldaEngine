package com.github.rmheuer.engine.gui;

public final class GuiTreeFlags {
    public static final int None = 0;
    
    public static final int Selected                = 1;
    public static final int ToggleRequiresSelection = 1 << 1;
    public static final int Leaf                    = 1 << 2;
    public static final int BackgroundFillsAvailX   = 1 << 3;
    public static final int DefaultOpen             = 1 << 4;

    private GuiTreeFlags() {
	throw new AssertionError();
    }
}
