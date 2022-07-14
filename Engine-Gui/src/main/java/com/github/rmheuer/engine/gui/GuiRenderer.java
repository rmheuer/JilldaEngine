package com.github.rmheuer.engine.gui;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.obj.Transient;
import com.github.rmheuer.engine.render2d.CompositeDrawList2D;
import com.github.rmheuer.engine.render2d.Rectangle;
import com.github.rmheuer.engine.render.texture.Texture;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Transient
public final class GuiRenderer implements WorldLocalSingleton {
    private static final class Pane {
        private Pane parent;

        private Rectangle bounds;
        private Rectangle contentBounds;
        private Vector2f cursor;
        private Rectangle widgetBounds;
        private float sameLineSpacing;
        private float indent;
        private boolean autoSizedX, autoSizedY;
        private float rowMaxY;
        private boolean disableLineSpacing;
        private boolean enablePaddingX, enablePaddingY;
        private Vector2f max;
        private boolean pushedClip;

        private Deque<Integer> treePushColumnStack;

        private Vector2f tableRowOrigin;
        private float[] tableColumnWidths;
        private float[] tableColumnIndents;
        private int tableColumnIndex;
        private int tableRowIndex;
        private boolean insideTable;
        private int tableFlags;

        private TabBar tabBar;
    }

    private static final class WindowData {
        private WindowData parent;

        private final GuiStorage storage;
        private Object id;
        private String title;
        private int flags;
        private Rectangle bounds;
        private boolean isFirstAppearing;

        private Vector2f prevSize;
        private Vector2f scroll;
        
        private CompositeDrawList2D draw;

        public WindowData(float x, float y) {
            bounds = Rectangle.fromXYSizes(x, y, 400, 400);
            storage = new GuiStorage();
            isFirstAppearing = true;
            prevSize = new Vector2f(0, 0);
            scroll = new Vector2f(0, 0);
        }
    }

    private final GuiStorage windows;
    private final List<Object> windowOrder;
    private final GuiInput input;
    private GuiStyle style;

    private final Deque<CompositeDrawList2D> drawStack;
    private CompositeDrawList2D mainDraw;
    private CompositeDrawList2D draw;
    private CompositeDrawList2D foreground;
    private Rectangle displayBounds;
    private Pane pane;
	private Rectangle widgetSizeOverride;
	private WindowData window;

    public GuiRenderer() {
        style = new GuiStyle();
        input = new GuiInput();
        windows = new GuiStorage();
        windowOrder = new ArrayList<>();
        drawStack = new ArrayDeque<>();
    }

    // --- Frame control ---

    public void beginFrame(int width, int height) {
        mainDraw = new CompositeDrawList2D();
        foreground = new CompositeDrawList2D();

        float halfW = width / 2.0f;
        float halfH = height / 2.0f;
        displayBounds = new Rectangle(-halfW, -halfH, halfW, halfH);
        pane = null;
        beginPane(new Rectangle(displayBounds), false, false, true, true, false);

        input.beginFrame(width, height);

        if (input.isAnyMouseButtonPressed()) {
            Object hoveredWindow = getHoveredWindow();
            if (hoveredWindow != null) {
                windowOrder.remove(hoveredWindow);
                windowOrder.add(hoveredWindow);
            }
        }
    }

    private void beginPane(Rectangle bounds, boolean autoSizeX, boolean autoSizeY, boolean padX, boolean padY, boolean pushClip) {
        Pane p = new Pane();
        p.parent = pane;
        p.bounds = bounds;
        p.contentBounds = bounds.shrink(padX ? style.padding * 2 : 0, padY ? style.padding * 2 : 0);
        p.cursor = new Vector2f(p.contentBounds.getMin());
        p.widgetBounds = null;
        p.sameLineSpacing = -1;
        p.indent = 0;
        p.autoSizedX = autoSizeX;
        p.autoSizedY = autoSizeY;
        p.rowMaxY = -Float.MAX_VALUE;
        p.treePushColumnStack = new ArrayDeque<>();
        p.insideTable = false;
        p.disableLineSpacing = false;
        p.enablePaddingX = padX;
        p.enablePaddingY = padY;
        p.max = new Vector2f(-Float.MAX_VALUE, -Float.MAX_VALUE);
        p.pushedClip = pushClip;

        pane = p;

        if (pushClip)
            draw.pushClip(pane.contentBounds);
    }

    private void endPane() {
        if (pane.pushedClip)
            draw.popClip();
        pane = pane.parent;
    }

    private void movePane(float x, float y) {
        pane.cursor.sub(pane.bounds.getMin()).add(x, y);
        pane.bounds.move(x, y);
        pane.contentBounds.move(x, y);
    }

    private void setPaneBounds(Rectangle bounds) {
        pane.cursor.sub(pane.bounds.getMin()).add(bounds.getMin());
        pane.bounds = bounds;
        pane.contentBounds = pane.bounds.shrink(pane.enablePaddingX ? style.padding * 2 : 0, pane.enablePaddingY ? style.padding * 2 : 0);
    }

    private void resizePane(float w, float h) {
        pane.bounds.resize(w, h);
        pane.contentBounds = pane.bounds.shrink(pane.enablePaddingX ? style.padding * 2 : 0, pane.enablePaddingY ? style.padding * 2 : 0);
    }

    public void endFrame() {
        windowOrder.removeAll(windows.getUnusedKeys());
        windows.disposeUnused();

        // Put all the windows into the main draw list
        Set<Object> invalid = new HashSet<>();
        for (Object title : windowOrder) {
            WindowData win = windows.get(title);
            if (win == null)
                invalid.add(title);
            else
                mainDraw.join(win.draw);
        }
        windowOrder.removeAll(invalid);
        mainDraw.join(foreground);

        input.endFrame();
    }

    public CompositeDrawList2D getDrawList() {
        return mainDraw;
    }

    public void pushDraw() { pushDraw(new CompositeDrawList2D()); }
    public void pushDraw(CompositeDrawList2D d) {
        if (draw != null)
            drawStack.push(draw);
        draw = d;
    }

    public void popDraw() {
        draw = drawStack.pollFirst();
    }

    // --- Layout ---

    public void sameLine() {
        sameLine(style.font.textWidth(" "));
    }

    public void sameLine(float spacing) {
        pane.sameLineSpacing = spacing;
    }

    public void indent() {
        indent(textHeight());
    }

    public void indent(float amount) {
        if (pane.parent != null && pane.parent.insideTable) {
            pane.parent.tableColumnIndents[pane.parent.tableColumnIndex] += amount;
        } else {
            pane.indent += amount;
        }
    }

    public void unindent() {
        unindent(textHeight());
    }

    public void unindent(float amount) {
        indent(-amount);
    }

    public void setLineSpacingEnabled(boolean enabled) {
        pane.disableLineSpacing = !enabled;
    }

    public float availContentWidth() {
        return pane.contentBounds.getMax().x - pane.cursor.x;
    }

    private void advanceCursor() {
        if (pane.sameLineSpacing < 0) {
            float indent;
            if (pane.parent != null && pane.parent.insideTable) {
                indent = pane.parent.tableColumnIndents[pane.parent.tableColumnIndex];
            } else {
                indent = pane.indent;
            }

            pane.cursor.x = pane.contentBounds.getMin().x + indent - window.scroll.x;
            if (pane.widgetBounds != null) {
                pane.cursor.y = pane.rowMaxY + (pane.disableLineSpacing ? 0 : style.linePadding);
                pane.rowMaxY = -Float.MAX_VALUE;
            }
        } else {
            if (pane.widgetBounds != null) {
                pane.cursor.x += pane.widgetBounds.getWidth() + pane.sameLineSpacing;
            }
            pane.sameLineSpacing = -1;
        }
    }

    private void plotBounds(Rectangle bounds) {
    	widgetSizeOverride = null;
        pane.widgetBounds = bounds;
        Vector2f max = pane.contentBounds.getMax();
        if (pane.autoSizedX) {
            max.x = Math.max(max.x, bounds.getMax().x);
        }
        if (pane.autoSizedY) {
            max.y = Math.max(max.y, bounds.getMax().y);
        }

        pane.max.max(bounds.getMax());

        pane.rowMaxY = Math.max(pane.rowMaxY, bounds.getMax().y);

        // TODO: Maybe only recalculate if content bounds changed
        pane.bounds = pane.contentBounds.expand(pane.enablePaddingX ? style.padding * 2 : 0, pane.enablePaddingY ? style.padding * 2 : 0);

//        r.outlineQuad(bounds.getMin().x, bounds.getMin().y, bounds.getWidth(), bounds.getHeight(), 1, new Vector4f(0, 1, 1, 1));

//        r.outlineQuad(pane.bounds.getMin().x, pane.bounds.getMin().y, pane.bounds.getWidth(), pane.bounds.getHeight(), 1, new Vector4f(0, 1, 0, 1));
//        r.outlineQuad(pane.contentBounds.getMin().x, pane.contentBounds.getMin().y, pane.contentBounds.getWidth(), pane.contentBounds.getHeight(), 1, new Vector4f(0, 1, 1, 1));
    }

    private void beginWidget(Vector2f size) {
        advanceCursor();
        plotBounds(new Rectangle(new Vector2f(pane.cursor), new Vector2f(pane.cursor).add(size)));
    }

    public Vector2f getCursor() {
        return pane.cursor;
    }

    public Rectangle getWidgetBounds() {
        return new Rectangle(pane.widgetBounds);
    }

    public Rectangle getContentBounds() {
        return new Rectangle(pane.contentBounds);
    }

    // --- Input ---

    private Object getHoveredWindow() {
        for (int i = windowOrder.size() - 1; i >= 0; i--) {
            if (input.isMouseInRectOverride(((WindowData) windows.get(windowOrder.get(i))).bounds)) {
                return windowOrder.get(i);
            }
        }
        return null;
    }

    public boolean isWidgetHovered() {
        return input.isMouseInRect(widgetSizeOverride != null ? widgetSizeOverride : pane.widgetBounds);
    }

    public boolean isWindowHovered() {
        return window.id.equals(getHoveredWindow());
    }

    public boolean isWidgetClicked(MouseButton button) {
        return isWidgetHovered() && input.isMouseButtonPressed(button);
    }

    public boolean isWidgetHeld(MouseButton button) {
        return isWidgetHovered() && input.isMouseButtonHeld(button);
    }

    public boolean isClickedOutsideRect(Rectangle rect) {
        return !input.isMouseInRect(rect) && input.isAnyMouseButtonPressed();
    }

    private boolean isClickedOutsideRectOverride(Rectangle rect) {
        return !input.isMouseInRectOverride(rect) && input.isAnyMouseButtonPressedOverride();
    }

    // --- Windows ---

    // axis: 0 is x, 1 is y
    // TODO: Redo this
    private float scrollBar(int axis) {
        int other = axis == 0 ? 1 : 0;

        float paneSize = axis == 0 ? pane.bounds.getWidth() : pane.bounds.getHeight();
        float contentSize = window.prevSize.getComponent(axis);

        float frac = paneSize / contentSize;
        float height = Math.max(paneSize * frac, style.scrollBarMinSize);

        float halfW = style.scrollBarWidth / 2;
        float halfH = height / 2;

        float minAxis = pane.bounds.getMin().getComponent(axis) + height / 2;
        float maxAxis = pane.bounds.getMax().getComponent(axis) - height / 2;
        float centerOther = pane.bounds.getMax().getComponent(other) - style.padding / 2;
        float centerAxis = MathUtils.map(window.scroll.getComponent(axis), 0, contentSize - paneSize + style.padding, minAxis, maxAxis);

        Rectangle rect;
        if (axis == 0) {
            rect = Rectangle.fromXYSizes(centerAxis - halfH, centerOther - halfW, height, style.scrollBarWidth);
        } else {
            rect = Rectangle.fromXYSizes(centerOther - halfW, centerAxis - halfH, style.scrollBarWidth, height);
        }

        Vector4f color;
        if (input.isMouseInRect(rect)) {
            color = style.scrollBarHoverColor;
        } else {
            color = style.scrollBarColor;
        }

        draw.fillRoundedQuad(rect, style.scrollBarRounding, color);

        Vector2f drag = input.getDragInRect(rect, MouseButton.LEFT);
        return drag.getComponent(axis) / (maxAxis - minAxis) * (contentSize - paneSize);
    }

    public boolean isWindowFocused() {
        return windowOrder.get(windowOrder.size() - 1).equals(window.id);
    }

    public void beginWindow(String title) {
        beginWindowFlags(title, title, GuiWindowFlags.None);
    }

    private void beginWindow(String title, Object id) { beginWindowFlags(title, id, GuiWindowFlags.None); }

    public void beginWindowFlags(String title, int flags) { beginWindowFlags(title, title, flags); }

    private void beginWindowFlags(String title, Object id, int flags) {
        WindowData prevWindow = window;
        window = windows.get(id, () -> {
            WindowData win = new WindowData(
                    displayBounds.getMin().x + displayBounds.getWidth() * (float) Math.random(),
                    displayBounds.getMin().y + displayBounds.getHeight() * (float) Math.random()
            );

            windowOrder.add(title);
            return win;
        });
        window.parent = prevWindow;
        window.draw = new CompositeDrawList2D();
        pushDraw(window.draw);
        window.title = title;
        window.id = id;
        window.flags = flags;

        boolean autoX = hasFlag(flags, GuiWindowFlags.AutoSizeX);
        boolean autoY = hasFlag(flags, GuiWindowFlags.AutoSizeY);
        boolean prevHover = isWindowHovered();
        window.bounds.resize(
                autoX ? 0 : window.bounds.getWidth(),
                autoY ? 0 : window.bounds.getHeight()
        );
        beginPane(window.bounds, autoX, autoY, true, true, false);

        pane.cursor.sub(window.scroll);

        if (!hasFlag(flags, GuiWindowFlags.NoTitleBar)) {
            Rectangle b = new Rectangle(
                    new Vector2f(window.bounds.getMin()),
                    new Vector2f(window.bounds.getMax().x, window.bounds.getMin().y + style.titleBarHeight)
            );
            setPaneBounds(new Rectangle(b.getMin().x, b.getMax().y, window.bounds.getMax().x, window.bounds.getMax().y));
        }

        pushDraw();
        draw.pushClip(pane.contentBounds);
        input.pushEnableState((autoX || autoY) ? prevHover : isWindowHovered(), isWindowFocused());
    }

    private void clampScroll() {
        window.scroll.x = MathUtils.clamp(window.scroll.x, 0, Math.max(0, window.prevSize.x - pane.bounds.getWidth() + style.padding));
        window.scroll.y = MathUtils.clamp(window.scroll.y, 0, Math.max(0, window.prevSize.y - pane.bounds.getHeight() + style.padding));
    }

    private boolean hasFlag(int flags, int flag) {
        return (flags & flag) != 0;
    }

    public void endWindow() {
        draw.popClip();
        window.prevSize = pane.max.sub(pane.bounds.getMin()).add(window.scroll);

        CompositeDrawList2D contentDraw = draw;
        popDraw();

        boolean focused = isWindowFocused();

        int flags = window.flags;

        boolean popup = hasFlag(flags, GuiWindowFlags._IsPopup);

        boolean titleBar = !hasFlag(flags, GuiWindowFlags.NoTitleBar);
        boolean border = !hasFlag(flags, GuiWindowFlags.NoBorder);
        boolean background = !hasFlag(flags, GuiWindowFlags.NoBackground);

        boolean resize = !hasFlag(flags, GuiWindowFlags.NoResize);
        boolean move = !hasFlag(flags, GuiWindowFlags.NoMove);
        boolean scrollX = !hasFlag(flags, GuiWindowFlags.NoScrollX);
        boolean scrollY = !hasFlag(flags, GuiWindowFlags.NoScrollY);

        boolean autoX = hasFlag(flags, GuiWindowFlags.AutoSizeX);
        boolean autoY = hasFlag(flags, GuiWindowFlags.AutoSizeY);

        if (autoX || autoY) {
            window.bounds = new Rectangle(pane.bounds.getMin().x, pane.bounds.getMin().y - (titleBar ? style.titleBarHeight : 0), pane.bounds.getMax().x, pane.bounds.getMax().y);
        }

        Rectangle titleBarBounds = null;
        if (titleBar) {
            Rectangle b = new Rectangle(
                    new Vector2f(window.bounds.getMin()),
                    new Vector2f(window.bounds.getMax().x, window.bounds.getMin().y + style.titleBarHeight)
            );
            titleBarBounds = b;
            draw.fillRoundedQuad(b, style.windowRounding, style.windowRounding, 0, 0, focused ? style.titleBarActiveColor : style.titleBarColor);
            draw.drawText(window.title, b.getMin().x + style.padding, b.getMidpoint().y, 0, 0.5f, style.font, style.textColor);
        }

        if (background) {
            Vector4f color = popup ? style.popupBackgroundColor : style.backgroundColor;
            if (titleBar)
                draw.fillRoundedQuad(pane.bounds, 0, 0, style.windowRounding, style.windowRounding, color);
            else
                draw.fillRoundedQuad(pane.bounds, style.windowRounding, color);
        }

        draw.join(contentDraw);

        if (scrollX && window.prevSize.x > pane.bounds.getWidth()) {
            window.scroll.x += scrollBar(0);
        }

        if (scrollY && window.prevSize.y > pane.bounds.getHeight()) {
            window.scroll.y += scrollBar(1);
        }

        clampScroll();

        if (border) {
            Vector4f color = popup ? style.popupBorderColor : style.borderColor;
            draw.drawRoundedQuad(window.bounds, style.windowRounding, style.borderWidth, color);

            if (titleBar) {
                Rectangle b = titleBarBounds;
                draw.drawLine(b.getMin().x, b.getMax().y, b.getMax().x - 1, b.getMax().y, style.borderWidth, color);
            }
        }

        if (resize) {
            Vector2f cursorPos = input.getPrevCursorPos();

            Vector2f max = new Vector2f(window.bounds.getMax()).sub(1, 1);
            float size = style.padding * 2;
            boolean hovered = (cursorPos.x - max.x) + (cursorPos.y - max.y) >= -size && cursorPos.x <= max.x && cursorPos.y <= max.y;

            Vector4f color;
            if (hovered) {
                color = style.resizeGrabHoverColor;
            } else {
                color = style.resizeGrabColor;
            }

            draw.fillTriangle(max.x, max.y - size, max.x - size, max.y, max, color);

            if (hovered) {
                Vector2f drag = input.getDrag(MouseButton.LEFT);
                if (drag.x != 0 || drag.y != 0) {
                    Vector2f winSize = window.bounds.getSize().add(drag);

                    // Hard limit for size
                    winSize.x = Math.max(100 + style.titleBarHeight, winSize.x);
                    winSize.y = Math.max(100, winSize.y);

                    setWindowSize(winSize.x, winSize.y);
                    input.consumeDrag(MouseButton.LEFT);
                }
            }
        }

        if (titleBar && move) {
            Rectangle b = new Rectangle(
                    new Vector2f(window.bounds.getMin()),
                    new Vector2f(window.bounds.getMax().x, window.bounds.getMin().y + style.titleBarHeight)
            );
            Vector2f drag = input.getDragInRect(b, MouseButton.LEFT);
            setWindowPos(window.bounds.getMin().x + drag.x, window.bounds.getMin().y + drag.y);
        }

        Vector2f scroll = input.getScrollInRect(pane.bounds);
        window.scroll.sub(scroll);

        if (popup && isClickedOutsideRectOverride(window.bounds)) {
            windowOrder.remove(window.id);
            windows.delete(window.id);
        }

        clampScroll();
        endPane();
        window.isFirstAppearing = false;
        window.storage.disposeUnused();
        window = window.parent;
        popDraw();
        input.popEnableState();
    }

    public void setWindowSize(float width, float height) {
        setWindowSize(width, height, GuiWindowCond.Always);
    }

    public void setWindowSize(float width, float height, GuiWindowCond cond) {
        if (checkInverseWindowCond(cond))
            return;

        resizePane(width, height);
        window.bounds.resize(width, height);
    }

    public void setWindowPos(float x, float y) {
        setWindowPos(x, y, 0, 0);
    }

    public void setWindowPos(float x, float y, GuiWindowCond cond) {
        setWindowPos(x, y, 0, 0, cond);
    }

    public void setWindowPos(float x, float y, float alignX, float alignY) {
        setWindowPos(x, y, alignX, alignY, GuiWindowCond.Always);
    }

    public void setWindowPos(float x, float y, float alignX, float alignY, GuiWindowCond cond) {
        if (checkInverseWindowCond(cond))
            return;

        float w = window.bounds.getWidth();
        float h = window.bounds.getHeight();
        x -= alignX * w;
        y -= alignY * h;

        movePane(x, y);
        window.bounds.move(x, y);
    }

    private boolean checkInverseWindowCond(GuiWindowCond cond) {
        switch (cond) {
            case Always:
                return false;
            case Appearing:
                return !window.isFirstAppearing;
            default:
                throw new IllegalArgumentException("Invalid window condition");
        }
    }

    public CompositeDrawList2D getWindowDrawList() {
        return window.draw;
    }

    public CompositeDrawList2D getActiveDrawList() {
        return draw;
    }

    public float getScrollY() {
        return window.scroll.y;
    }

    public void setScrollY(float y) {
        window.scroll.y = y;
        clampScroll();
    }

    // --- ID stack ---

    public void pushId(Object id) {
        window.storage.push(id);
    }

    public void popId() {
        window.storage.pop();
    }

    public void deleteStorage(Object id) {
        window.storage.delete(id);
    }

    // --- Widgets ---

    public void spacing() {
        float size = textHeight();
        spacing(size, size);
    }

    public void spacing(float width, float height) {
        spacing(new Vector2f(width, height));
    }

    public void spacing(Vector2f size) {
        beginWidget(size);
    }

    public void separator() {
        beginWidget(new Vector2f(availContentWidth(), style.separatorSize));

        float y = pane.widgetBounds.getMidpoint().y;
        draw.drawLine(pane.widgetBounds.getMin().x, y, pane.widgetBounds.getMax().x, y, style.borderWidth, style.separatorColor);
    }

    public void beginChild() {
        advanceCursor();
        beginPane(new Rectangle(new Vector2f(pane.cursor), new Vector2f(pane.cursor)), true, true, true, true, true);
    }

    public void beginChild(float width, float height) {
        beginChild(new Vector2f(width, height));
    }

    public void beginChild(Vector2f size) {
        beginChild(size, false, false, true, true);
    }

    private void beginChild(Vector2f size, boolean autoX, boolean autoY, boolean padX, boolean padY) {
        advanceCursor();
        Rectangle bounds = new Rectangle(new Vector2f(pane.cursor), new Vector2f(pane.cursor).add(size));
        beginPane(bounds, autoX, autoY, padX, padY, true);
    }

    public void endChild() {
        Rectangle bounds = pane.bounds;
        endPane();

        // Account for the area the child consumed
        plotBounds(bounds);
    }

    public void text(String text) {
        beginWidget(new Vector2f(style.font.textWidth(text), style.font.getMetrics().getHeight()));
        draw.drawText(text, pane.cursor.x, pane.cursor.y, 0, 0, style.font, style.textColor);
    }

    public boolean button(String text) {
        float width = style.font.textWidth(text) + style.buttonPadding.x * 2;
        float height = textHeight() + style.buttonPadding.y * 2;

        beginWidget(new Vector2f(width, height));

        Vector4f color;
        if (isWidgetHeld(MouseButton.LEFT)) {
            color = style.buttonPressColor;
        } else if (isWidgetHovered()) {
            color = style.buttonHoverColor;
        } else {
            color = style.buttonColor;
        }

        float rad = style.buttonRounding;
        float b = style.borderWidth;
        Vector2f cursor = pane.cursor;
        draw.fillRoundedQuad(cursor.x + b, cursor.y + b, width - b * 2, height - b * 2, rad, color);
        draw.drawRoundedQuad(cursor.x, cursor.y, width, height, rad, b, style.buttonBorderColor);

        draw.drawText(text, cursor.x + width / 2, cursor.y + height / 2, 0.5f, 0.5f, style.font, style.textColor);

        return isWidgetClicked(MouseButton.LEFT);
    }

    public boolean imageButton(Texture img, float width, float height, Vector2f uv0, Vector2f uv1) {
        beginWidget(new Vector2f(width, height));

        draw.drawImage(pane.cursor.x, pane.cursor.y, width, height, img, uv0.x, uv0.y, uv1.x, uv1.y);

        return isWidgetClicked(MouseButton.LEFT);
    }

    public void image(Texture img, float width, float height, Vector2f uv0, Vector2f uv1) {
        beginWidget(new Vector2f(width, height));
        draw.drawImage(pane.cursor.x, pane.cursor.y, width, height, img, uv0.x, uv0.y, uv1.x, uv1.y);
    }

    // Important: Only use for sizes smaller than text height
    public void icon(Texture img, float width, float height, Vector2f uv0, Vector2f uv1) {
        float textHeight = textHeight();
        beginWidget(new Vector2f(width, textHeight()));

        draw.drawImage(pane.cursor.x, pane.cursor.y + textHeight / 2 - height / 2, width, height, img, uv0.x, uv0.y, uv1.x, uv1.y);
    }

    public boolean editString(StringBuilder str) { return editString(str, str); }
    public boolean editString(StringBuilder str, int flags) { return editString(str, str, flags); }
    public boolean editString(StringBuilder str, TextFilter filter) { return editString(str, str, filter, GuiTextEditFlags.None); }
    public boolean editString(StringBuilder str, TextFilter filter, int flags) { return editString(str, str, filter, flags); }
    public boolean editString(StringBuilder str, Object id) { return editString(str, id, GuiTextEditFlags.None); }
    public boolean editString(StringBuilder str, Object id, int flags) { return editString(str, id, (s) -> true, flags); }

    private static final class TextEditState {
        boolean focused;
        StringBuilder buffer;
        int cursor;
        int selectStart, selectEnd;

        TextEditState() {
            focused = false;
            cursor = 0;
        }

        boolean hasSelection() {
            return selectStart != selectEnd;
        }

        void deleteSelection() {
            int min = Math.min(selectStart, selectEnd);
            int max = Math.max(selectStart, selectEnd);
            buffer.delete(min, max);
            cursor = selectStart = selectEnd = min;
        }
    }

    public boolean editString(StringBuilder str, Object id, TextFilter filter, int flags) {
        TextEditState state = window.storage.get(id, TextEditState::new);

        boolean returnFocused = hasFlag(flags, GuiTextEditFlags.ReturnFocused);
        boolean alignToTreeSize = hasFlag(flags, GuiTextEditFlags.AlignToTreeSize);
        boolean forceFocus = hasFlag(flags, GuiTextEditFlags.Focus);
        if (forceFocus) {
            state.focused = true;
            state.buffer = new StringBuilder(str);
        }
        boolean selectAll = hasFlag(flags, GuiTextEditFlags.SelectAll);
        if (state.focused && selectAll) {
            state.selectStart = 0;
            state.selectEnd = state.buffer.length();
        }

        Vector2f padding = alignToTreeSize ? new Vector2f(style.textEditPadding.x, style.linePadding / 2) : style.textEditPadding;
        float width = availContentWidth();
        float height = textHeight() + 2 * padding.y;
        beginWidget(new Vector2f(width, height));

        boolean ret = false;
        StringBuilder string = state.focused ? state.buffer : str;

        float contentScroll = 0;

        if (state.focused) {
            // Cursor controls
            boolean cursorMoved = false;
            if (!state.hasSelection()) {
                state.selectStart = state.selectEnd = state.cursor;
            }

            if (input.isKeyPressed(Key.LEFT)) {
                state.cursor--;
                cursorMoved = true;
            }
            if (input.isKeyPressed(Key.RIGHT)) {
                state.cursor++;
                cursorMoved = true;
            }
            if (state.cursor < 0) state.cursor = 0;
            if (state.cursor > string.length()) state.cursor = string.length();
            if (cursorMoved) {
                boolean shift = input.isShiftHeld();

                if (!shift) {
                    state.selectStart = state.selectEnd = state.cursor;
                } else {
                    state.selectEnd = state.cursor;
                }
            }

            // Delete
            if (input.isKeyPressed(Key.BACKSPACE)) {
                if (state.hasSelection()) {
                    state.deleteSelection();
                } else if (state.cursor > 0) {
                    string.deleteCharAt(state.cursor - 1);
                    state.cursor--;
                }
            }

            // Insert
            String textInput = input.getTextInput();
            if (!textInput.equals("")) {
                input.consumeTextInput();
                if (state.hasSelection()) {
                    state.deleteSelection();
                }

                string.insert(state.cursor, textInput);
                state.cursor += textInput.length();
            }

            // TODO: Calculate scroll to cursor
        }
        boolean textAllowed = filter.passes(string.toString());

        // Mouse input
        boolean hovered = isWidgetHovered();
        boolean pressed = isWidgetHeld(MouseButton.LEFT);
        boolean clicked = isWidgetClicked(MouseButton.LEFT);
        boolean clickedOutside = isClickedOutsideRectOverride(pane.widgetBounds);

        // Focus
        if (state.focused) {
            if (input.isKeyPressed(Key.ESCAPE)) {
                state.focused = false;
            } else if (clickedOutside || input.isKeyPressed(Key.ENTER)) {
                state.focused = false;
                if (textAllowed) {
                    ret = true;
                    str.replace(0, str.length(), state.buffer.toString());
                }
            }
        } else if (clicked) {
            state.focused = true;
            state.buffer = new StringBuilder(str);
        }

        if (pressed) {
            // Find where the text was clicked with binary search
            float relativeX = input.getCursorPos().x - pane.cursor.x - style.textEditPadding.x - contentScroll;
            int min = 0, max = string.length();
            while (true) {
                int mid = (min + max) / 2;
                float textWidth = style.font.textWidth(string.substring(0, mid));
                if (relativeX > textWidth) {
                    min = mid;
                } else if (relativeX < textWidth) {
                    max = mid;
                } else {
                    state.cursor = mid;
                    break;
                }

                if (max - min <= 1) {
                    float minOff = Math.abs(style.font.textWidth(string.substring(0, min)) - relativeX);
                    float maxOff = Math.abs(style.font.textWidth(string.substring(0, max)) - relativeX);

                    if (minOff < maxOff) {
                        state.cursor = min;
                    } else {
                        state.cursor = max;
                    }

                    break;
                }
            }

            if (!selectAll) {
                if (clicked) {
                    state.selectStart = state.cursor;
                }
                state.selectEnd = state.cursor;
            }
        }

        // Background
        Vector4f background;
        if (!textAllowed) {
            background = style.textEditInvalidColor;
        } else if (state.focused) {
            background = style.textEditFocusColor;
        } else if (hovered) {
            background = style.textEditHoverColor;
        } else {
            background = style.textEditColor;
        }

        float b = style.borderWidth;
        float halfHeight = textHeight() / 2;
        float centerY = pane.widgetBounds.getMidpoint().y;
        draw.fillRoundedQuad(pane.cursor.x + b, pane.cursor.y + b, width - 2*b, height - 2*b, style.textEditRounding, background);
        draw.drawRoundedQuad(pane.cursor.x, pane.cursor.y, width, height, style.textEditRounding, style.borderWidth, style.textEditBorderColor);
        draw.pushClip(new Rectangle(pane.cursor.x + padding.x, pane.cursor.y, pane.widgetBounds.getMax().x - padding.x, pane.widgetBounds.getMax().y));
        if (state.focused && state.hasSelection()) {
            int min = Math.min(state.selectStart, state.selectEnd);
            int max = Math.max(state.selectStart, state.selectEnd);
            float minX = pane.cursor.x + padding.x + style.font.textWidth(string.substring(0, min));
            float w = style.font.textWidth(string.substring(min, max));
            draw.fillQuad(minX, centerY - halfHeight, w, textHeight(), style.textEditSelectionColor);
        }
        draw.drawText(string.toString(), pane.cursor.x + padding.x - contentScroll, pane.widgetBounds.getMidpoint().y, 0, 0.5f, style.font, style.textColor);
        if (state.focused) {
            float cursorX = pane.cursor.x + padding.x + style.font.textWidth(string.substring(0, state.cursor)) - contentScroll;
            draw.drawLine(cursorX, centerY - halfHeight, cursorX, centerY + halfHeight, 1, style.textEditCursorColor);
        }
        draw.popClip();

        return returnFocused ? state.focused : ret;
    }

    // Returns offset on X to align object of height1 with object of height2
    private float centerAlign(float height1, float height2) {
        if (height2 < height1) {
            return 0;
        }

        return height2 / 2.0f - height1 / 2.0f;
    }

    public boolean pushTree(String label) {
        return pushTree(label, label);
    }

    public boolean pushTree(String label, Object id) {
        return pushTree(label, id, GuiTreeFlags.None);
    }

    public boolean pushTree(String label, Object id, int flags) {
        boolean selected = hasFlag(flags, GuiTreeFlags.Selected);
        boolean toggleRequiresSelection = hasFlag(flags, GuiTreeFlags.ToggleRequiresSelection);
        boolean leaf = hasFlag(flags, GuiTreeFlags.Leaf);
        boolean backgroundFillsAvailX = hasFlag(flags, GuiTreeFlags.BackgroundFillsAvailX);

        float padding = style.linePadding / 2;
        float widthOfSpace = style.font.textWidth(" ");

        float w = style.treeIconSize + style.font.textWidth(label) + widthOfSpace + padding * 2;
        float h = Math.max(textHeight(), style.treeIconSize) + 2 * padding;

        beginWidget(new Vector2f(w, h));

        Rectangle bgRect;
		if (backgroundFillsAvailX) {
			bgRect = Rectangle.fromXYSizes(pane.contentBounds.getMin().x, pane.cursor.y, pane.contentBounds.getWidth(), h);
		} else {
			bgRect = Rectangle.fromXYSizes(pane.cursor.x, pane.cursor.y, w, h);
		}

        widgetSizeOverride = bgRect;
        boolean hovered = isWidgetHovered();
		boolean pressed = isWidgetHeld(MouseButton.LEFT);
		boolean clicked = isWidgetClicked(MouseButton.LEFT);

        Vector4f bgColor;
        if (selected) {
            bgColor = style.treeSelectionColor;
        } else if (pressed) {
            bgColor = style.treePressColor;
        } else if (hovered) {
            bgColor = style.treeHoverColor;
        } else {
            bgColor = style.treeColor;
        }

        draw.fillQuad(bgRect, bgColor);

        boolean defaultState = (flags & GuiTreeFlags.DefaultOpen) != 0;
        boolean[] open = window.storage.get(id, () -> new boolean[]{defaultState});
        if (!leaf) {
            // If not requiring selection, always check for click
            // Otherwise only if selected
            if ((!toggleRequiresSelection || selected) && clicked) {
                open[0] = !open[0];
            }

            // Draw triangle icon
            Rectangle iconBounds = Rectangle.fromXYSizes(pane.cursor.x + padding, pane.cursor.y + padding + centerAlign(style.treeIconSize, textHeight()), style.treeIconSize, style.treeIconSize);
            Vector2f min = iconBounds.getMin();
            Vector2f max = iconBounds.getMax();
            Vector2f midpoint = iconBounds.getMidpoint();
            if (open[0]) {
                // Triangle pointing down
                draw.fillTriangle(
                        min,
                        max.x, min.y,
                        midpoint.x, max.y,
                        style.treeIconColor
                );
            } else {
                // Triangle pointing right
                draw.fillTriangle(
                        min,
                        max.x, midpoint.y,
                        min.x, max.y,
                        style.treeIconColor
                );
            }
        }

        float cursorToLabelSpacing = treeCursorToLabelSpacing();

        // Draw label
        draw.drawText(label, pane.cursor.x + cursorToLabelSpacing, pane.cursor.y + padding + centerAlign(textHeight(), style.treeIconSize), 0, 0, style.font, style.textColor);

        if (!leaf && open[0]) {
            indent(cursorToLabelSpacing);
            if (pane.parent != null && pane.parent.insideTable)
                pane.parent.treePushColumnStack.push(pane.parent.tableColumnIndex);
            pushId(id);
            return true;
        }

        return false;
    }

    private float treeCursorToLabelSpacing() {
        float padding = style.linePadding / 2;
        float widthOfSpace = style.font.textWidth(" ");
        return padding + style.treeIconSize + widthOfSpace;
    }

    public void popTree() {
        popId();
        boolean table = pane.parent != null && pane.parent.insideTable;
        if (table) {
            int column = pane.parent.treePushColumnStack.pop();
            pane.parent.tableColumnIndents[column] -= treeCursorToLabelSpacing();
        } else {
            unindent(treeCursorToLabelSpacing());
        }
    }

    public void beginTable(float... columnWeights) {
        beginTableFlags(GuiTableFlags.None, columnWeights);
    }

    // TODO: Allow pixel sized columns
    public void beginTableFlags(int flags, float... columnWeights) {
        advanceCursor();
        float availWidth = availContentWidth();
        Rectangle bounds = new Rectangle(new Vector2f(pane.cursor), new Vector2f(pane.cursor).add(availWidth, 0));
        beginPane(bounds, false, true, false, false, true);

        float totalWeight = 0;
        for (float weight : columnWeights)
            totalWeight += weight;

        float[] widths = new float[columnWeights.length];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = availWidth * columnWeights[i] / totalWeight;
        }

        pane.tableColumnWidths = widths;
        pane.tableColumnIndents = new float[columnWeights.length];
        pane.tableRowIndex = 0;
        pane.tableColumnIndex = 0;
        pane.insideTable = true;
        pane.tableRowOrigin = new Vector2f(pane.cursor);
        pane.tableFlags = flags;
        pane.disableLineSpacing = true;
    }

    public void tableNextColumn() {
        if (!pane.insideTable) {
            endChild();
        }

        if (pane.tableColumnIndex != 0) {
            sameLine(0);
        }

        float columnWidth = pane.tableColumnWidths[pane.tableColumnIndex];
        pane.tableColumnIndex++;

        if (pane.tableColumnIndex == pane.tableColumnWidths.length) {
            pane.tableColumnIndex = 0;
            pane.tableRowIndex++;

            if ((pane.tableFlags & GuiTableFlags.NoBorder) == 0) {
                float x = pane.tableRowOrigin.x;
                for (int i = 0; i < pane.tableColumnWidths.length; i++) {
                    float width = pane.tableColumnWidths[i];
                    draw.drawQuad(x, pane.tableRowOrigin.y, width, pane.rowMaxY - pane.tableRowOrigin.y, 1, new Vector4f(1, 1, 1, 1));
                    x += width;
                }
            }
        }

        Pane temp = pane;
        beginChild(new Vector2f(columnWidth, 0), false, true, (temp.tableFlags & GuiTableFlags.NoPaddingX) == 0, (temp.tableFlags & GuiTableFlags.NoPaddingY) == 0);
        if (temp.tableColumnIndex == 1) {
            temp.tableRowOrigin = new Vector2f(pane.bounds.getMin());
        }
    }

    public void endTable() {
        if (!pane.insideTable) {
            endChild();
        }
        pane.insideTable = false;

        Rectangle bounds = pane.bounds;
        endPane();
        plotBounds(bounds);
    }

    public int tableGetRow() {
        return pane.tableRowIndex;
    }

    public int tableGetColumn() {
        return pane.tableColumnIndex;
    }

    public void beginTooltip() {
        Vector2f origin = input.getCursorPos();
        pushDraw();
        beginPane(Rectangle.fromXYSizes(origin.x, origin.y, 0, 0), true, true, true, true, true);
    }

    public void endTooltip() {
        Rectangle bgRect = pane.bounds;
        endPane();

        foreground.fillQuad(bgRect, style.tooltipBackgroundColor);
        foreground.drawQuad(bgRect, style.borderWidth, style.tooltipBorderColor);
        foreground.join(draw);
        popDraw();
    }

    private String contextMenuTitle(String title) {
        return "__ctx:" + title;
    }

    // Use this to attach a context menu to the previous widget
    // Returns whether the context menu was opened on the previous widget
    public boolean addContextMenu(String title) {
        title = contextMenuTitle(title);
        return addContextMenu((Object) title);
    }

    private boolean addContextMenu(Object id) {
        boolean prevWidgetClicked = isWidgetClicked(MouseButton.RIGHT);
        if (prevWidgetClicked) {
            openContextMenu(id);
        }

        return prevWidgetClicked;
    }

    private void openContextMenu(Object id) {
        Vector2f cursorPos = input.getCursorPos();
        WindowData win = new WindowData(cursorPos.x - 2, cursorPos.y - 2);
        windows.set(id, win);
        windowOrder.add(id);
    }

    // Use this to begin context menu content.
    // Only call endContextMenu if this returns true!
    public boolean beginContextMenu(String title) {
        title = contextMenuTitle(title);

        return beginContextMenuId(title);
    }

    private boolean beginContextMenuId(Object id) {
        WindowData popup = windows.get(id);
        if (popup != null) {
            beginWindowFlags("context-menu", id, GuiWindowFlags._Popup);
            return true;
        }

        return false;
    }

    public void endContextMenu() {
        endWindow();
    }

    public void closeContextMenu() {
        windowOrder.remove(window.id);
        windows.delete(window.id);
    }

    private static final class TabItem {
        private final Object id;
        private String title;

        public TabItem(Object id, String title) {
            this.id = id;
            this.title = title;
        }
    }

    private static final class TabBar {
        private final Map<Object, TabItem> prevTabs;
        private final Map<Object, TabItem> currTabs;
        private final List<Object> tabOrder;
        private final Vector2f drawOrigin;
        private TabItem currentTab;

        public TabBar() {
            prevTabs = new HashMap<>();
            currTabs = new HashMap<>();
            tabOrder = new ArrayList<>();
            drawOrigin = new Vector2f();
        }
    }

    public void beginTabBar(Object id) {
        pane.tabBar = window.storage.get(id, TabBar::new);
        for (Map.Entry<Object, TabItem> entry : pane.tabBar.currTabs.entrySet()) {
            pane.tabBar.prevTabs.put(entry.getKey(), entry.getValue());
        }

        spacing(0, style.tabHeight);
        pane.tabBar.drawOrigin.set(pane.contentBounds.getMin().x, pane.cursor.y);
    }

    public void endTabBar() {
        for (Object key : pane.tabBar.prevTabs.keySet()) {
            pane.tabBar.tabOrder.remove(key);
        }

        Vector2f pos = pane.tabBar.drawOrigin;
        float h = style.tabHeight;
        Rectangle bounds = Rectangle.fromXYSizes(pos.x, pos.y, availContentWidth(), h);

        Vector2f pad = style.tabPadding;
        float ddWidth = pad.x * 2 + style.tabDropdownSize;

        float x = pos.x;
        TabItem currHover = null, prevHover = null;
        boolean allTabsFit = true;
        boolean drewCurrent = false;
        int i;
        for (i = 0; i < pane.tabBar.tabOrder.size(); i++) {
            Object id = pane.tabBar.tabOrder.get(i);
            boolean isLast = i == pane.tabBar.tabOrder.size() - 1;

            TabItem item = pane.tabBar.currTabs.get(id);
            float w = style.font.textWidth(item.title) + pad.x * 2;

            if (x + w > (isLast ? bounds.getMax().x : bounds.getMax().x - ddWidth)) {
                allTabsFit = false;
                break;
            }

            boolean active = pane.tabBar.currentTab.id.equals(item.id);
            Rectangle tabBounds = Rectangle.fromXYSizes(x, pos.y, w, h);
            boolean hover = input.isMouseInRect(tabBounds);

            if (hover)
                currHover = item;
            if (input.wasMouseInRect(tabBounds))
                prevHover = item;

            if (hover && input.isMouseButtonPressed(MouseButton.LEFT))
                pane.tabBar.currentTab = item;

            draw.fillRoundedQuad(x, pos.y, w, h, style.tabRounding, style.tabRounding, 0, 0, active ? style.tabActiveColor : style.tabColor);
            draw.drawText(item.title, x + w / 2, pos.y + h / 2, 0.5f, 0.5f, style.font, style.textColor);
            x += w;

            if (id.equals(pane.tabBar.currentTab.id))
                drewCurrent = true;
        }
        draw.drawLine(pos.x, pos.y + h, pos.x + availContentWidth(), pos.y + h, style.borderWidth, style.tabActiveColor);

        if (!allTabsFit) {
            float w = style.tabDropdownSize + pad.x * 2;
            Rectangle ddBounds = Rectangle.fromXYSizes(bounds.getMax().x - w, pos.y, w, h);
            draw.fillRoundedQuad(ddBounds, style.tabRounding, style.tabRounding, 0, 0, style.tabColor);

            float size = style.tabDropdownSize;
            Vector2f ddPos = new Vector2f(bounds.getMax().x - w / 2 - size / 2, pos.y + centerAlign(size, h));
            draw.fillTriangle(
                    ddPos,
                    ddPos.x + size, ddPos.y,
                    ddPos.x + size / 2, ddPos.y + size,
                    style.tabDropdownColor
            );

            if (input.isMouseInRect(ddBounds) && input.isMouseButtonPressed(MouseButton.LEFT)) {
                openContextMenu(pane.tabBar);
            }
        }

        if (input.isMouseButtonHeld(MouseButton.LEFT) && input.isMouseInRect(bounds) && prevHover != null && currHover != null && prevHover != currHover) {
            int currIndex = pane.tabBar.tabOrder.indexOf(currHover.id);
            int prevIndex = pane.tabBar.tabOrder.indexOf(prevHover.id);
            Collections.swap(pane.tabBar.tabOrder, currIndex, prevIndex);
        }

        if (!drewCurrent) {
            Object id = pane.tabBar.currentTab.id;
            pane.tabBar.tabOrder.remove(id);
            pane.tabBar.tabOrder.add(0, id);
        }

        TabBar bar = pane.tabBar;
        if (beginContextMenuId(bar)) {
            for (; i < bar.tabOrder.size(); i++) {
                Object tabId = bar.tabOrder.get(i);
                TabItem tab = bar.currTabs.get(tabId);
                if (button(tab.title)) {
                    bar.currentTab = tab;
                    closeContextMenu();
                }
            }
            endContextMenu();
        }

        pane.tabBar = null;
    }

    public boolean beginTabBarItem(String title) { return beginTabBarItem(title, title); }
    public boolean beginTabBarItem(String title, Object id) {
        TabItem prevTab = pane.tabBar.prevTabs.get(id);
        if (prevTab == null)
            prevTab = new TabItem(id, title);
        else
            pane.tabBar.prevTabs.remove(id);
        prevTab.title = title;
        pane.tabBar.currTabs.put(id, prevTab);

        if (pane.tabBar.currentTab == null)
            pane.tabBar.currentTab = prevTab;
        if (!pane.tabBar.tabOrder.contains(id))
            pane.tabBar.tabOrder.add(id);

        return pane.tabBar.currentTab.id.equals(id);
    }

    public void endTabBarItem() {

    }

    // Convenience method
    private float textHeight() {
        return style.font.getMetrics().getHeight();
    }

    // --- Getters ---

    public GuiStyle getStyle() {
        return style;
    }

    public GuiInput getInput() {
        return input;
    }

    // Important: Only windows with a String ID are saved.
    public SerialObject save() {
        SerialObject obj = new SerialObject();
        for (Object id : windowOrder) {
            if (!(id instanceof String))
                continue;

            String title = (String) id;
            WindowData value = windows.get(title);
            SerialObject win = new SerialObject();
            win.putFloat("x", value.bounds.getMin().x);
            win.putFloat("y", value.bounds.getMin().y);
            win.putFloat("w", value.bounds.getWidth());
            win.putFloat("h", value.bounds.getHeight());
            obj.put(title, win);
        }
        return obj;
    }

    public void load(SerialObject node) {
        for (String title : node.keySet()) {
            SerialObject win = node.getSerialObject(title);

            WindowData value = new WindowData(
                    win.getFloat("x"),
                    win.getFloat("y")
            );
            value.bounds.resize(
                    win.getFloat("w"),
                    win.getFloat("h")
            );

            windows.set(title, value);
            windowOrder.add(title);
        }
    }

    public void delete() {
        style.delete();
    }
}
