package com.github.rmheuer.engine.gui;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render2d.font.Font;
import com.github.rmheuer.engine.render2d.font.TrueTypeFont;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public final class GuiStyle {
    private final Font defaultFont;
    private final Deque<Font> fontStack;
    public Font font;

    public Vector4f borderColor;
    public Vector4f titleBarColor;
    public Vector4f titleBarActiveColor;
    public Vector4f backgroundColor;
    public Vector4f scrollBarColor;
    public Vector4f scrollBarHoverColor;
    public Vector4f resizeGrabColor;
    public Vector4f resizeGrabHoverColor;
    public Vector4f textColor;

    public Vector4f buttonBorderColor;
    public Vector4f buttonColor;
    public Vector4f buttonHoverColor;
    public Vector4f buttonPressColor;
    public Vector4f textEditColor;
    public Vector4f textEditHoverColor;
    public Vector4f textEditFocusColor;
    public Vector4f textEditInvalidColor;
    public Vector4f textEditBorderColor;
    public Vector4f textEditSelectionColor;
    public Vector4f textEditCursorColor;
    public Vector4f treeIconColor;
    public Vector4f treeColor;
    public Vector4f treeHoverColor;
    public Vector4f treePressColor;
    public Vector4f treeSelectionColor;

    public float windowRounding;
    public float titleBarHeight;
    public float scrollBarWidth;
    public float scrollBarRounding;
    public float scrollBarMinSize;
    public float padding;
    public float linePadding;
    public float borderWidth;
    public Vector2f buttonPadding;
    public float buttonRounding;
    public Vector2f textEditPadding;
    public float textEditRounding;
    public float treeIconSize;

    public GuiStyle(RenderBackend backend) {
        try {
            font = new TrueTypeFont(
                    backend,
                    new JarResourceFile("com/github/rmheuer/engine/gui/default-font.ttf"),
                    16
            );
            defaultFont = font;
            defaultFont.claim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default font", e);
        }
        fontStack = new ArrayDeque<>();

        windowRounding = 0;
        titleBarHeight = 6 + font.getMetrics().getHeight();
        scrollBarWidth = 8;
        scrollBarRounding = 4;
        scrollBarMinSize = 50;
        padding = 10;
        linePadding = 4;
        borderWidth = 1;
        buttonPadding = new Vector2f(6, 4);
        buttonRounding = 6;
        textEditPadding = new Vector2f(6, 4);
        textEditRounding = 6;
        treeIconSize = 12;
        setDark();
    }

    private Vector4f rgb(int r, int g, int b) {
        return new Vector4f(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
    }

    private Vector4f rgba(int r, int g, int b, int a) {
        return new Vector4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    public void setDark() {
        borderColor     = new Vector4f(0.08f, 0.10f, 0.12f, 1.0f);
        titleBarColor = rgb(20, 60, 100);
        titleBarActiveColor = rgb(28, 80, 140);
        backgroundColor = new Vector4f(0.11f, 0.15f, 0.17f, 1.0f);
        scrollBarColor = new Vector4f(0.20f, 0.25f, 0.29f, 1.00f);
        scrollBarHoverColor = new Vector4f(0.18f, 0.22f, 0.25f, 1.00f);
        resizeGrabColor = new Vector4f(0.26f, 0.59f, 0.98f, 0.25f);
        resizeGrabHoverColor = new Vector4f(0.26f, 0.59f, 0.98f, 0.67f);
        textColor       = new Vector4f(0.95f, 0.96f, 0.98f, 1.0f);

        buttonBorderColor = rgb(37, 37, 37);
        buttonColor = new Vector4f(0.20f, 0.25f, 0.29f, 1.0f);
        buttonHoverColor = rgb(28, 80, 140);
        buttonPressColor = rgb(20, 60, 100);
        textEditBorderColor = rgb(37, 37, 37);
        textEditColor = new Vector4f(0.20f, 0.25f, 0.29f, 1.0f);
        textEditHoverColor = rgb(28, 80, 140);
        textEditInvalidColor = rgb(140, 80, 28);
        textEditFocusColor = rgb(20, 60, 100);
        textEditSelectionColor = rgb(28, 80, 140);
        textEditCursorColor = new Vector4f(0.95f, 0.96f, 0.98f, 1.0f);
        treeIconColor = new Vector4f(0.95f, 0.96f, 0.98f, 1.0f);
        treeColor       = new Vector4f(0, 0, 0, 0);
        treeHoverColor = rgb(20, 60, 100);
        treePressColor = rgb(15, 50, 85);
        treeSelectionColor = rgb(28, 80, 140);
    }

    public void pushFont(Font font) {
        fontStack.push(this.font);
        this.font = font;
        font.claim();
    }

    public void popFont() {
        font.release();
        font = fontStack.pollFirst();
    }

    public void delete() {
        defaultFont.release();
    }
}
