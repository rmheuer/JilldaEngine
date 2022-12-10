package com.github.rmheuer.engine.gui;

public final class GuiWindow {
    private final String title;
    private final GuiRenderable content;

    public GuiWindow(String title, GuiRenderable content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public GuiRenderable getContent() {
        return content;
    }
}
