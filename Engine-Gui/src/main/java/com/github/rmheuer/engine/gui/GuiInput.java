package com.github.rmheuer.engine.gui;

import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.util.Pair;
import com.github.rmheuer.engine.render2d.Rectangle;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Set;

public final class GuiInput {
    private final Deque<Pair<Boolean, Boolean>> enableStack;
    private final Set<MouseButton> pressedButtons;
    private final Set<MouseButton> heldButtons;
    private final Set<MouseButton> consumedDrag;
    private final Set<Key> pressedKeys;
    private final Set<Key> heldKeys;
    private final Vector2f cursorOffset;
    private final Vector2f prevCursorPos;
    private final Vector2f cursorPos;
    private final Vector2f scroll;
    private String textInput;
    private boolean hoverEnabled;
    private boolean buttonsEnabled;

    public GuiInput() {
        enableStack = new ArrayDeque<>();
        pressedButtons = EnumSet.noneOf(MouseButton.class);
        heldButtons = EnumSet.noneOf(MouseButton.class);
        consumedDrag = EnumSet.noneOf(MouseButton.class);
        pressedKeys = EnumSet.noneOf(Key.class);
        heldKeys = EnumSet.noneOf(Key.class);
        cursorPos = new Vector2f(0, 0);
        cursorOffset = new Vector2f(0, 0);
        scroll = new Vector2f(0, 0);
        prevCursorPos = new Vector2f(-Float.MAX_VALUE, -Float.MAX_VALUE);
        textInput = "";
        hoverEnabled = true;
        buttonsEnabled = true;
    }

    public void pushEnableState(boolean hover, boolean buttons) {
        enableStack.push(new Pair<>(hoverEnabled, buttonsEnabled));
        hoverEnabled = hover;
        buttonsEnabled = buttons;
    }

    public void popEnableState() {
        Pair<Boolean, Boolean> pair = enableStack.pop();
        hoverEnabled = pair.getA();
        buttonsEnabled = pair.getB();
    }

    public boolean isMouseInRect(Rectangle rect) {
        return hoverEnabled && rect.containsPoint(cursorPos);
    }

    public boolean wasMouseInRect(Rectangle rect) {
        return hoverEnabled && rect.containsPoint(prevCursorPos);
    }

    // Ignores whether hover detection is allowed
    public boolean isMouseInRectOverride(Rectangle rect) {
        return rect.containsPoint(cursorPos);
    }

    public boolean isMouseButtonPressed(MouseButton button) {
        return buttonsEnabled && pressedButtons.contains(button);
    }

    public boolean isAnyMouseButtonPressed() {
        return buttonsEnabled && !pressedButtons.isEmpty();
    }

    public boolean isAnyMouseButtonPressedOverride() {
        return !pressedButtons.isEmpty();
    }

    public boolean isMouseButtonHeld(MouseButton button) {
        return buttonsEnabled && heldButtons.contains(button);
    }

    public boolean isKeyPressed(Key key) {
        return buttonsEnabled && pressedKeys.contains(key);
    }

    public boolean isShiftHeld() {
        return buttonsEnabled && heldKeys.contains(Key.LEFT_SHIFT) || heldKeys.contains(Key.RIGHT_SHIFT);
    }

    public String getTextInput() {
        if (!buttonsEnabled)
            return "";
        return textInput;
    }

    public void consumeTextInput() {
        if (buttonsEnabled)
            textInput = "";
    }

    public Vector2f getDrag(MouseButton button) {
        if (!buttonsEnabled || consumedDrag.contains(button) || !heldButtons.contains(button))
            return new Vector2f(0, 0);

        return new Vector2f(cursorPos).sub(prevCursorPos);
    }

    public Vector2f getDragInRect(Rectangle rect, MouseButton button) {
        if (buttonsEnabled && rect.containsPoint(prevCursorPos)) {
            return getDrag(button);
        }

        return new Vector2f(0, 0);
    }

    public Vector2f getScrollInRect(Rectangle rect) {
        if (!hoverEnabled || !rect.containsPoint(cursorPos))
            return new Vector2f(0, 0);

        return scroll;
    }

    public void consumeDrag(MouseButton button) {
        if (hoverEnabled)
            consumedDrag.add(button);
    }

    public Vector2f getCursorPos() {
        return cursorPos;
    }

    public Vector2f getPrevCursorPos() {
        return prevCursorPos;
    }

    // --- Event handling ---

    public void mouseButtonPressed(MouseButton button) {
        pressedButtons.add(button);
        heldButtons.add(button);
    }

    public void mouseButtonReleased(MouseButton button) {
        heldButtons.remove(button);
    }

    public void mouseScrolled(float pixelsX, float pixelsY) {
        scroll.add(pixelsX, pixelsY);
    }

    public void mouseMoved(float posX, float posY) {
        cursorPos.set(posX, posY);
    }

    public void keyPressedOrRepeated(Key key) {
        pressedKeys.add(key);
        heldKeys.add(key);
    }

    public void keyReleased(Key key) {
        heldKeys.remove(key);
    }

    public void charTyped(char c) {
        textInput += c;
    }

    public void beginFrame() {
//        cursorOffset.set(-width / 2.0f, -height / 2.0f);
        cursorOffset.set(0, 0);
    }

    public void endFrame() {
        pressedButtons.clear();
        pressedKeys.clear();
        consumedDrag.clear();
        prevCursorPos.set(cursorPos);
        scroll.set(0, 0);
        textInput = "";
    }
}
