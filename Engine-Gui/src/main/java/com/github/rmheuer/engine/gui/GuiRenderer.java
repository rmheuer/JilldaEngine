package com.github.rmheuer.engine.gui;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.obj.Transient;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render2d.CompositeDrawList2D;
import com.github.rmheuer.engine.render2d.Rectangle;
import com.github.rmheuer.engine.render2d.Renderer2D;
import com.github.rmheuer.engine.render.texture.Texture;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    }

    private static final class WindowData {
        private final GuiStorage storage;
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

    private final Map<String, WindowData> windows;
    private final Set<String> unusedWindows;
    private final List<String> windowOrder;
    private final GuiInput input;
    private GuiStyle style;

    private CompositeDrawList2D mainDraw;
    private Rectangle displayBounds;
    private Pane pane;
	private Rectangle widgetSizeOverride;
	private WindowData window;

    public GuiRenderer(RenderBackend backend) {
        style = new GuiStyle(backend);
        input = new GuiInput();
        windows = new HashMap<>();
        unusedWindows = new HashSet<>();
        windowOrder = new ArrayList<>();
    }

    // --- Frame control ---

    public void beginFrame(int width, int height) {
        mainDraw = new CompositeDrawList2D();

        float halfW = width / 2.0f;
        float halfH = height / 2.0f;
        displayBounds = new Rectangle(-halfW, -halfH, halfW, halfH);
        pane = null;
        beginPane(new Rectangle(displayBounds), false, false, true, true, false);

        input.beginFrame(width, height);

        if (input.isMouseButtonPressed(MouseButton.LEFT)) {
            String hoveredWindow = getHoveredWindow();
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
            window.draw.pushClip(pane.contentBounds);
    }

    private void endPane() {
        if (pane.pushedClip)
            window.draw.popClip();
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
        for (String title : unusedWindows) {
            windows.remove(title);
            windowOrder.remove(title);
        }
        unusedWindows.clear();
        unusedWindows.addAll(windows.keySet());

        // Put all the windows into the main draw list
        for (String title : windowOrder) {
            WindowData win = windows.get(title);
            mainDraw.join(win.draw);
        }

	input.endFrame();
    }

    public CompositeDrawList2D getDrawList() {
        return mainDraw;
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

    private float availContentWidth() {
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

    // --- Input ---

    private String getHoveredWindow() {
        for (int i = windowOrder.size() - 1; i >= 0; i--) {
            if (input.isMouseInRect(windows.get(windowOrder.get(i)).bounds)) {
                return windowOrder.get(i);
            }
        }
        return null;
    }

    public boolean isWidgetHovered() {
        return input.isMouseInRect(widgetSizeOverride != null ? widgetSizeOverride : pane.widgetBounds);
    }

    public boolean isWidgetClicked(MouseButton button) {
        return isWidgetHovered() && input.isMouseButtonPressed(button);
    }

    public boolean isWidgetHeld(MouseButton button) {
        return isWidgetHovered() && input.isMouseButtonHeld(button);
    }

    // --- Windows ---

    public void beginWindow(String title) {
        beginWindowFlags(title, GuiWindowFlags.None);
    }

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

        window.draw.fillRoundedQuad(rect, style.scrollBarRounding, color);

        Vector2f drag = input.getDragInRect(rect, MouseButton.LEFT);
        return drag.getComponent(axis) / (maxAxis - minAxis) * (contentSize - paneSize);
    }

    public boolean isWindowFocused() {
        return windowOrder.get(windowOrder.size() - 1).equals(window.title);
    }

    public void beginWindowFlags(String title, int flags) {
        unusedWindows.remove(title);
        if (!windows.containsKey(title)) {
            WindowData win = new WindowData(
                    displayBounds.getMin().x + displayBounds.getWidth() * (float) Math.random(),
                    displayBounds.getMin().y + displayBounds.getHeight() * (float) Math.random()
            );

            windowOrder.add(title);
            windows.put(title, win);
        }
        window = windows.get(title);
        window.draw = new CompositeDrawList2D();
        window.title = title;
        window.flags = flags;
        beginPane(window.bounds, false, false, true, true, false);

        boolean focused = isWindowFocused();

        boolean titleBar = !hasFlag(flags, GuiWindowFlags.NoTitleBar);
        boolean border = !hasFlag(flags, GuiWindowFlags.NoBorder);
        boolean background = !hasFlag(flags, GuiWindowFlags.NoBackground);

        boolean scrollX = !hasFlag(flags, GuiWindowFlags.NoScrollX);
        boolean scrollY = !hasFlag(flags, GuiWindowFlags.NoScrollY);

        Rectangle titleBarBounds = null;
        if (titleBar) {
            Rectangle b = new Rectangle(
                    new Vector2f(window.bounds.getMin()),
                    new Vector2f(window.bounds.getMax().x, window.bounds.getMin().y + style.titleBarHeight)
            );
            titleBarBounds = b;
            window.draw.fillRoundedQuad(b, style.windowRounding, style.windowRounding, 0, 0, focused ? style.titleBarActiveColor : style.titleBarColor);

            drawText(window.title, b.getMin().x + style.padding, b.getMidpoint().y, 0, 0.5f);

            setPaneBounds(new Rectangle(b.getMin().x, b.getMax().y, window.bounds.getMax().x, window.bounds.getMax().y));
        }

        if (background) {
            if (titleBar)
                window.draw.fillRoundedQuad(pane.bounds, 0, 0, style.windowRounding, style.windowRounding, style.backgroundColor);
            else
                window.draw.fillRoundedQuad(pane.bounds, style.windowRounding, style.backgroundColor);
        }

        if (scrollX && window.prevSize.x > pane.bounds.getWidth()) {
            window.scroll.x += scrollBar(0);
        }

        if (scrollY && window.prevSize.y > pane.bounds.getHeight()) {
            window.scroll.y += scrollBar(1);
        }

        clampScroll();
        pane.cursor.sub(window.scroll);

        if (border) {
            window.draw.drawRoundedQuad(window.bounds, style.windowRounding, style.borderWidth, style.borderColor);

            if (titleBar) {
                Rectangle b = titleBarBounds;
                window.draw.drawLine(b.getMin().x, b.getMax().y, b.getMax().x - 1, b.getMax().y, style.borderWidth, style.borderColor);
            }
        }

        window.draw.pushClip(pane.contentBounds);
    }

    private void clampScroll() {
        window.scroll.x = MathUtils.clamp(window.scroll.x, 0, Math.max(0, window.prevSize.x - pane.bounds.getWidth() + style.padding));
        window.scroll.y = MathUtils.clamp(window.scroll.y, 0, Math.max(0, window.prevSize.y - pane.bounds.getHeight() + style.padding));
    }

    private boolean hasFlag(int flags, int flag) {
        return (flags & flag) != 0;
    }

    public void endWindow() {
        window.draw.popClip();
        window.prevSize = pane.max.sub(pane.bounds.getMin()).add(window.scroll);

        if (!hasFlag(window.flags, GuiWindowFlags.NoResize)) {
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

            window.draw.fillTriangle(max.x, max.y - size, max.x - size, max.y, max, color);

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

        // Move scroll bars here so they render on top of content and so resize grab can capture drag event

        if (!hasFlag(window.flags, GuiWindowFlags.NoTitleBar) && !hasFlag(window.flags, GuiWindowFlags.NoMove)) {
            Rectangle b = new Rectangle(
                    new Vector2f(window.bounds.getMin()),
                    new Vector2f(window.bounds.getMax().x, window.bounds.getMin().y + style.titleBarHeight)
            );
            Vector2f drag = input.getDragInRect(b, MouseButton.LEFT);
            setWindowPos(window.bounds.getMin().x + drag.x, window.bounds.getMin().y + drag.y);
        }

        Vector2f scroll = input.getScrollInRect(pane.bounds);
        window.scroll.sub(scroll);

        clampScroll();
        endPane();
        window.isFirstAppearing = false;
        window.storage.disposeUnused();
        window = null;
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

    public void pushId(Object id) {
        window.storage.push(id);
    }

    public void popId() {
        window.storage.pop();
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
        drawText(text, pane.cursor.x, pane.cursor.y, 0, 0);
    }

    private void drawText(String text, float x, float y, float alignX, float alignY) {
        float width = style.font.textWidth(text);
        float ascent = style.font.getMetrics().getAscent();
        float height = style.font.getMetrics().getHeight();

        window.draw.drawText(text, x - width * alignX, y + ascent - height * alignY, style.font, style.textColor);
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
        window.draw.fillRoundedQuad(cursor.x + b, cursor.y + b, width - b * 2, height - b * 2, rad, color);
        window.draw.drawRoundedQuad(cursor.x, cursor.y, width, height, rad, b, style.buttonBorderColor);

        drawText(text, cursor.x + width / 2, cursor.y + height / 2, 0.5f, 0.5f);

        return isWidgetClicked(MouseButton.LEFT);
    }

    public boolean imageButton(Texture img, float width, float height, Vector2f uv0, Vector2f uv1) {
        beginWidget(new Vector2f(width, height));

        window.draw.drawImage(pane.cursor.x, pane.cursor.y, width, height, img, uv0.x, uv0.y, uv1.x, uv1.y);

        return isWidgetClicked(MouseButton.LEFT);
    }

    public void image(Texture img, float width, float height, Vector2f uv0, Vector2f uv1) {
        beginWidget(new Vector2f(width, height));
        window.draw.drawImage(pane.cursor.x, pane.cursor.y, width, height, img, uv0.x, uv0.y, uv1.x, uv1.y);
    }

    // Important: Only use for sizes smaller than text height
    public void icon(Texture img, float width, float height, Vector2f uv0, Vector2f uv1) {
        float textHeight = textHeight();
        beginWidget(new Vector2f(width, textHeight()));

        window.draw.drawImage(pane.cursor.x, pane.cursor.y + textHeight / 2 - height / 2, width, height, img, uv0.x, uv0.y, uv1.x, uv1.y);
    }

    public boolean editString(StringBuilder str) {
        return editString(str, str);
    }

    public boolean editString(StringBuilder str, TextFilter filter) {
        return editString(str, str, filter);
    }

    public boolean editString(StringBuilder str, Object id) {
        return editString(str, id, (s) -> true);
    }

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

    public boolean editString(StringBuilder str, Object id, TextFilter filter) {
        TextEditState state = window.storage.get(id, TextEditState::new);

        float width = availContentWidth();
        float height = textHeight() + 2 * style.textEditPadding.y;
        beginWidget(new Vector2f(width, height));

        boolean changed = false;
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
        boolean clickedOutside = !input.isMouseInRect(pane.widgetBounds) && input.isMouseButtonPressed(MouseButton.LEFT);

        // Focus
        if (state.focused) {
            if (input.isKeyPressed(Key.ESCAPE)) {
                state.focused = false;
            } else if (clickedOutside || input.isKeyPressed(Key.ENTER)) {
                state.focused = false;
                if (textAllowed) {
                    changed = true;
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

            if (clicked) {
                state.selectStart = state.cursor;
            }
            state.selectEnd = state.cursor;
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
        window.draw.fillRoundedQuad(pane.cursor.x + b, pane.cursor.y + b, width - 2*b, height - 2*b, style.textEditRounding, background);
        window.draw.drawRoundedQuad(pane.cursor.x, pane.cursor.y, width, height, style.textEditRounding, style.borderWidth, style.textEditBorderColor);
        window.draw.pushClip(new Rectangle(pane.cursor.x + style.textEditPadding.x, pane.cursor.y, pane.widgetBounds.getMax().x - style.textEditPadding.x, pane.widgetBounds.getMax().y));
        if (state.focused && state.hasSelection()) {
            int min = Math.min(state.selectStart, state.selectEnd);
            int max = Math.max(state.selectStart, state.selectEnd);
            float minX = pane.cursor.x + style.textEditPadding.x + style.font.textWidth(string.substring(0, min));
            float w = style.font.textWidth(string.substring(min, max));
            window.draw.fillQuad(minX, centerY - halfHeight, w, textHeight(), style.textEditSelectionColor);
        }
        drawText(string.toString(), pane.cursor.x + style.textEditPadding.x - contentScroll, pane.widgetBounds.getMidpoint().y, 0, 0.5f);
        if (state.focused) {
            float cursorX = pane.cursor.x + style.textEditPadding.x + style.font.textWidth(string.substring(0, state.cursor)) - contentScroll;
            window.draw.drawLine(cursorX, centerY - halfHeight, cursorX, centerY + halfHeight, 1, style.textEditCursorColor);
        }
        window.draw.popClip();

        return changed;
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

        window.draw.fillQuad(bgRect, bgColor);

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
                window.draw.fillTriangle(
                        min,
                        max.x, min.y,
                        midpoint.x, max.y,
                        style.treeIconColor
                );
            } else {
                // Triangle pointing right
                window.draw.fillTriangle(
                        min,
                        max.x, midpoint.y,
                        min.x, max.y,
                        style.treeIconColor
                );
            }
        }

        float cursorToLabelSpacing = treeCursorToLabelSpacing();

        // Draw label
        drawText(label, pane.cursor.x + cursorToLabelSpacing, pane.cursor.y + padding + centerAlign(textHeight(), style.treeIconSize), 0, 0);

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
                    window.draw.drawQuad(x, pane.tableRowOrigin.y, width, pane.rowMaxY - pane.tableRowOrigin.y, 1, new Vector4f(1, 1, 1, 1));
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

    public SerialObject save() {
        SerialObject obj = new SerialObject();
        for (String title : windowOrder) {
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

            windows.put(title, value);
            windowOrder.add(title);
        }
    }
}
