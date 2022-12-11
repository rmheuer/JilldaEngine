package com.github.rmheuer.engine.render;

public final class WindowSettings {
    private int width;
    private int height;
    private String title;
    private boolean resizable;
    private boolean forceAspectRatio;

    public WindowSettings() {
        width = 800;
        height = 600;
        title = "Game";
        resizable = true;
        forceAspectRatio = true;
    }

    public int getWidth() {
        return width;
    }

    public WindowSettings setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public WindowSettings setHeight(int height) {
        this.height = height;
        return this;
    }

    public WindowSettings setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public WindowSettings setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isResizable() {
        return resizable;
    }

    public WindowSettings setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public boolean isForceAspectRatio() {
        return forceAspectRatio;
    }

    public void setForceAspectRatio(boolean forceAspectRatio) {
        this.forceAspectRatio = forceAspectRatio;
    }
}
