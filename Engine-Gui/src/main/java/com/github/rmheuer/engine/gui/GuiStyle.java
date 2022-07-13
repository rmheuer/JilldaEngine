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
    public Vector4f separatorColor;

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
    public Vector4f tooltipBackgroundColor;
    public Vector4f tooltipBorderColor;
    public Vector4f popupBackgroundColor;
    public Vector4f popupBorderColor;

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
    public float separatorSize;

    public GuiStyle() {
        try {
            font = new TrueTypeFont(
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
        separatorSize = 10;
        setLight();
    }

    private Vector4f rgba(int r, int g, int b, int a) {
        return new Vector4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    private Vector4f invrgba(int r, int g, int b, int a) {
        return new Vector4f(1 - r / 255.0f, 1 - g / 255.0f, 1 - b / 255.0f, a / 255.0f);
    }

    public void setDark() {
        borderColor            = rgba( 20,  26,  31, 255);
        titleBarColor          = rgba( 20,  60, 100, 255);
        titleBarActiveColor    = rgba( 28,  80, 140, 255);
        backgroundColor        = rgba( 28,  38,  43, 255);
        scrollBarColor         = rgba( 51,  64,  74, 255);
        scrollBarHoverColor    = rgba( 46,  56,  64, 255);
        resizeGrabColor        = rgba( 66, 150, 250, 64);
        resizeGrabHoverColor   = rgba( 66, 150, 250, 171);
        textColor              = rgba(242, 245, 250, 255);
        separatorColor         = rgba(242, 245, 250, 255);
        buttonBorderColor      = rgba( 37,  37,  37, 255);
        buttonColor            = rgba( 51,  64,  74, 255);
        buttonHoverColor       = rgba( 28,  80, 140, 255);
        buttonPressColor       = rgba( 20,  60, 100, 255);
        textEditBorderColor    = rgba( 37,  37,  37, 255);
        textEditColor          = rgba( 51,  64,  74, 255);
        textEditHoverColor     = rgba( 28,  80, 140, 255);
        textEditInvalidColor   = rgba(140,  80,  28, 255);
        textEditFocusColor     = rgba( 20,  60, 100, 255);
        textEditSelectionColor = rgba( 28,  80, 140, 255);
        textEditCursorColor    = rgba(242, 245, 250, 255);
        treeIconColor          = rgba(242, 245, 250, 255);
        treeColor              = rgba(  0,   0,   0,   0);
        treeHoverColor         = rgba( 20,  60, 100, 255);
        treePressColor         = rgba( 15,  50,  85, 255);
        treeSelectionColor     = rgba( 28,  80, 140, 255);
        tooltipBackgroundColor = rgba(  0,   0,   0, 255);
        tooltipBorderColor     = rgba(242, 245, 250, 255);
        popupBackgroundColor   = rgba(  0,   0,   0, 255);
        popupBorderColor       = rgba(242, 245, 250, 255);
    }

    public void setLight() {
        borderColor            = invrgba( 20,  26,  31, 255);
        titleBarColor          = invrgba( 20,  60, 100, 255);
        titleBarActiveColor    = invrgba( 28,  80, 140, 255);
        backgroundColor        = invrgba( 28,  38,  43, 255);
        scrollBarColor         = invrgba( 51,  64,  74, 255);
        scrollBarHoverColor    = invrgba( 46,  56,  64, 255);
        resizeGrabColor        = invrgba( 66, 150, 250,  64);
        resizeGrabHoverColor   = invrgba( 66, 150, 250, 171);
        textColor              = invrgba(242, 245, 250, 255);
        separatorColor         = invrgba(242, 245, 250, 255);
        buttonBorderColor      = invrgba( 37,  37,  37, 255);
        buttonColor            = invrgba( 51,  64,  74, 255);
        buttonHoverColor       = invrgba( 28,  80, 140, 255);
        buttonPressColor       = invrgba( 20,  60, 100, 255);
        textEditBorderColor    = invrgba( 37,  37,  37, 255);
        textEditColor          = invrgba( 51,  64,  74, 255);
        textEditHoverColor     = invrgba( 28,  80, 140, 255);
        textEditInvalidColor   = invrgba(140,  80,  28, 255);
        textEditFocusColor     = invrgba( 20,  60, 100, 255);
        textEditSelectionColor = invrgba( 28,  80, 140, 255);
        textEditCursorColor    = invrgba(242, 245, 250, 255);
        treeIconColor          = invrgba(242, 245, 250, 255);
        treeColor              = invrgba(  0,   0,   0,   0);
        treeHoverColor         = invrgba( 20,  60, 100, 255);
        treePressColor         = invrgba( 15,  50,  85, 255);
        treeSelectionColor     = invrgba( 28,  80, 140, 255);
        tooltipBackgroundColor = invrgba(  0,   0,   0, 255);
        tooltipBorderColor     = invrgba(242, 245, 250, 255);
        popupBackgroundColor   = invrgba(  0,   0,   0, 255);
        popupBorderColor       = invrgba(242, 245, 250, 255);
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
