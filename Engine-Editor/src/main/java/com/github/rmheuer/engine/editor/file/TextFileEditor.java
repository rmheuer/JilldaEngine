package com.github.rmheuer.engine.editor.file;

import com.github.rmheuer.engine.core.Time;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.gui.GuiInput;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiStyle;
import com.github.rmheuer.engine.render2d.CompositeDrawList2D;
import com.github.rmheuer.engine.render2d.Rectangle;
import com.github.rmheuer.engine.render2d.font.Font;
import com.github.rmheuer.engine.render2d.font.TrueTypeFont;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TextFileEditor implements FileEditor {
    private static final String FONT_PATH = "com/github/rmheuer/engine/editor/fonts/JetBrainsMono-Regular.ttf";
    private static final int AUTOSCROLL_THRESHOLD = 3;

    private final List<StringBuilder> lines;
    private final Font font;
    private ResourceFile res;
    private Vector2i cursorPos;
    private Vector2i selectionStart, selectionEnd;
    private int preferredX;
    private float cursorBlinkTimer;

    public TextFileEditor() {
        lines = new ArrayList<>();

        try {
            font = new TrueTypeFont(new JarResourceFile(FONT_PATH), 14);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load font");
        }

        reset();
    }

    private void reset() {
        cursorPos = new Vector2i(0, 0);
        selectionStart = new Vector2i(0, 0);
        selectionEnd = new Vector2i(0, 0);
        preferredX = -1;
        cursorBlinkTimer = 0;
    }

    @Override
    public void open(ResourceFile res) throws IOException {
        this.res = res;
        if (res == null)
            return;

        String content = res.readAsString();
        String[] split = content
                .replace("\r", "")
                .replace("\t", "        ")
                .split("\n");

        lines.clear();
        for (String line : split) {
            lines.add(new StringBuilder(line));
        }

        reset();
    }

    @Override
    public ResourceFile getCurrentFile() {
        return res;
    }

    @Override
    public void save() throws IOException {
        if (res == null)
            return;

        StringBuilder out = new StringBuilder();
        for (StringBuilder line : lines) {
            out.append(line).append("\n");
        }
        res.writeString(out.toString());
    }

    private boolean hasSelection() {
        return !selectionStart.equals(selectionEnd);
    }

    private void sortSelection() {
        boolean flipSel = selectionStart.y > selectionEnd.y || (selectionStart.y == selectionEnd.y && selectionStart.x > selectionEnd.x);
        Vector2i selectMin = flipSel ? selectionEnd : selectionStart;
        Vector2i selectMax = flipSel ? selectionStart : selectionEnd;
        selectionStart = selectMin;
        selectionEnd = selectMax;
    }

    private void deleteSelection() {
        sortSelection();
        for (int y = selectionStart.y; y <= selectionEnd.y; y++) {
            StringBuilder line = lines.get(y);
            int min = y == selectionStart.y ? selectionStart.x : 0;
            int max = y == selectionEnd.y ? selectionEnd.x : line.length();
            line.delete(min, max);

            if (y != selectionStart.y) {
                lines.remove(line);
                y--;
                lines.get(y).append(line);
                selectionEnd.y--;
            }
        }
        selectionEnd.set(selectionStart);
        cursorPos.set(selectionStart);
    }

    private void showEditor(GuiRenderer g, float beginY) {
        cursorBlinkTimer += Time.getDelta();
        if (cursorBlinkTimer > 1)
            cursorBlinkTimer -= 1;

        GuiStyle style = g.getStyle();
        GuiInput input = g.getInput();
        CompositeDrawList2D draw = g.getActiveDrawList();

        int lineCount = lines.size();
        if (lineCount == 0) {
            lines.add(new StringBuilder(""));
            lineCount = 1;
        }

        float fontWidth = font.textWidth("A"); // This assumes font is monospaced
        float fontHeight = font.getMetrics().getHeight();
        g.spacing(g.availContentWidth(), fontHeight * lineCount);
        Vector2f pos = g.getCursor();

        Rectangle bounds = g.getContentBounds();
        bounds = new Rectangle(bounds.getMin().x, Math.max(bounds.getMin().y, beginY), bounds.getMax().x, bounds.getMax().y);

        Vector2f localMousePos = new Vector2f(input.getCursorPos()).sub(pos);
        int hoveredTextY = MathUtils.clamp((int) (localMousePos.y / fontHeight), 0, lineCount - 1);
        int hoveredTextX = MathUtils.clamp((int) (localMousePos.x / fontWidth), 0, lines.get(hoveredTextY).length());

        // Click to plot selection start
        if (g.isWidgetClicked(MouseButton.LEFT)) {
            selectionStart.set(hoveredTextX, hoveredTextY);
            cursorBlinkTimer = 0;
            // Don't need to set cursor or selection end here, they are set in held
        }

        // Drag to select and move cursor
        if (g.isWidgetHeld(MouseButton.LEFT)) {
            selectionEnd.set(hoveredTextX, hoveredTextY);
            cursorPos.set(hoveredTextX, hoveredTextY);
        }

        boolean cursorMoved = false;
        if (!hasSelection()) {
            selectionStart.set(cursorPos);
            selectionEnd.set(cursorPos);
        }

        if (input.isKeyPressed(Key.LEFT)) {
            if (cursorPos.x > 0)
                cursorPos.x--;
            else if (cursorPos.y > 0) {
                cursorPos.y--;
                cursorPos.x = lines.get(cursorPos.y).length();
            }
            preferredX = -1;
            cursorMoved = true;
        }
        if (input.isKeyPressed(Key.RIGHT)) {
            if (cursorPos.x < lines.get(cursorPos.y).length())
                cursorPos.x++;
            else if (cursorPos.y < lines.size() - 1) {
                cursorPos.y++;
                cursorPos.x = 0;
            }
            preferredX = -1;
            cursorMoved = true;
        }
        if (input.isKeyPressed(Key.UP) && cursorPos.y > 0) {
            cursorPos.y--;
            if (preferredX < 0)
                preferredX = cursorPos.x;
            cursorPos.x = Math.min(preferredX, lines.get(cursorPos.y).length());
            cursorMoved = true;
        }
        if (input.isKeyPressed(Key.DOWN) && cursorPos.y < lines.size() - 1) {
            cursorPos.y++;
            if (preferredX < 0)
                preferredX = cursorPos.x;
            cursorPos.x = Math.min(preferredX, lines.get(cursorPos.y).length());
            cursorMoved = true;
        }
        boolean scrollToCursor = false;
        if (cursorMoved) {
            cursorBlinkTimer = 0;

            boolean shift = input.isShiftHeld();
            if (!shift)
                selectionStart.set(cursorPos);

            selectionEnd.set(cursorPos);
            scrollToCursor = true;
        }

        if (input.isKeyPressed(Key.BACKSPACE)) {
            if (hasSelection()) {
                deleteSelection();
            } else {
                StringBuilder line = lines.get(cursorPos.y);
                if (cursorPos.x > 0) {
                    line.deleteCharAt(--cursorPos.x);
                } else if (cursorPos.y > 0) {
                    StringBuilder prevLine = lines.get(--cursorPos.y);
                    cursorPos.x = lines.get(cursorPos.y).length();
                    prevLine.append(line);
                    lines.remove(line);
                }
            }
            cursorBlinkTimer = 0;
            scrollToCursor = true;
        }

        String textInput = input.getTextInput();
        if (input.isKeyPressed(Key.TAB)) textInput += "    ";
        if (!textInput.equals("")) {
            input.consumeTextInput();
            if (hasSelection())
                deleteSelection();

            lines.get(cursorPos.y).insert(cursorPos.x, textInput);
            cursorPos.x += textInput.length();

            cursorBlinkTimer = 0;
            scrollToCursor = true;
        }

        if (input.isKeyPressed(Key.ENTER)) {
            StringBuilder line = lines.get(cursorPos.y);
            String after = line.substring(cursorPos.x);

            if (cursorPos.x < line.length())
                line.delete(cursorPos.x, line.length());

            StringBuilder whitespace = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (!Character.isWhitespace(c))
                    break;
                whitespace.append(c);
            }

            cursorPos.y++;
            cursorPos.x = whitespace.length();
            StringBuilder newLine = new StringBuilder(whitespace);
            newLine.append(after);
            lines.add(cursorPos.y, newLine);

            selectionStart.set(cursorPos);
            selectionEnd.set(cursorPos);

            cursorBlinkTimer = 0;
            scrollToCursor = true;
        }

        // Autoscroll to keep cursor on screen
        int minRow = Math.max(0, (int) Math.floor((bounds.getMin().y - pos.y) / fontHeight));
        int maxRow = (int) Math.ceil((bounds.getMax().y - pos.y) / fontHeight);
        if (scrollToCursor) {
            float scroll = g.getScrollY();
            if (cursorPos.y < minRow + AUTOSCROLL_THRESHOLD) {
                int diff = cursorPos.y - (minRow + AUTOSCROLL_THRESHOLD);
                scroll += diff * fontHeight;
            }
            if (cursorPos.y > maxRow - AUTOSCROLL_THRESHOLD) {
                int diff = cursorPos.y - (maxRow - AUTOSCROLL_THRESHOLD);
                scroll += diff * fontHeight;
            }
            g.setScrollY(scroll);
        }

        // Draw selection
        boolean flipSel = selectionStart.y > selectionEnd.y || (selectionStart.y == selectionEnd.y && selectionStart.x > selectionEnd.x);
        Vector2i selectMin = flipSel ? selectionEnd : selectionStart;
        Vector2i selectMax = flipSel ? selectionStart : selectionEnd;
        float selFirstX = selectMin.x * fontWidth;
        float selLastX = selectMax.x * fontWidth;
        for (int i = selectMin.y; i <= selectMax.y; i++) {
            float minX = pos.x + (i == selectMin.y ? selFirstX : 0);
            float maxX = pos.x + (i == selectMax.y ? selLastX : g.availContentWidth());
            draw.fillQuad(minX, pos.y + i * fontHeight, maxX - minX, fontHeight, style.textEditSelectionColor);
        }

        // Draw text
        float y = pos.y + minRow * fontHeight;
        for (int i = minRow; i < maxRow && i < lines.size(); i++) {
            StringBuilder line = lines.get(i);
            draw.drawText(line.toString(), pos.x, y, 0, 0, font, style.textColor);
            y += fontHeight;
        }

        // Draw cursor
        if (cursorBlinkTimer < 0.5f) {
            StringBuilder cursorLine = lines.get(cursorPos.y);
            float cursorX = pos.x + font.textWidth(cursorLine.substring(0, cursorPos.x));
            float cursorY = pos.y + cursorPos.y * fontHeight;
            draw.drawLine(cursorX, cursorY, cursorX, cursorY + fontHeight, 1, style.textEditCursorColor);
        }
    }

    @Override
    public void showGui(GuiRenderer g) {
        if (res != null) {
            float beginY = g.getCursor().y;
            g.text("Editing " + res.getName());
            g.separator();
            showEditor(g, beginY);
        } else {
            g.text("No file open");
        }
    }
}
