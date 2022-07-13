package com.github.rmheuer.engine.gui;

import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.input.keyboard.CharTypeEvent;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.keyboard.KeyEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyPressEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyReleaseEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyRepeatEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.input.mouse.MouseButtonPressEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseButtonReleaseEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseMoveEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseScrollEvent;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.util.Pair;
import com.github.rmheuer.engine.render2d.Rectangle;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Set;

// TODO: Properly take camera transform and projection into account when finding cursor pos
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

    private void onMouseButtonPress(MouseButtonPressEvent event) {
        pressedButtons.add(event.getButton());
        heldButtons.add(event.getButton());
        onMouse(event);
    }

    private void onMouseButtonRelease(MouseButtonReleaseEvent event) {
        heldButtons.remove(event.getButton());
        onMouse(event);
    }

    private void onMouseScroll(MouseScrollEvent event) {
        scroll.add(event.getScrollPixelsX(), event.getScrollPixelsY());
        onMouse(event);
    }

    private void onMouse(MouseEvent event) {
        cursorPos.set(event.getX(), event.getY()).add(cursorOffset);
    }

    private void onKeyPressOrRepeat(KeyEvent event) {
        pressedKeys.add(event.getKey());
        heldKeys.add(event.getKey());
    }

    private void onKeyRelease(KeyReleaseEvent event) {
        heldKeys.remove(event.getKey());
    }

    private void onCharType(CharTypeEvent event) {
        textInput += event.getChar();
    }

    public void beginFrame(int width, int height) {
        cursorOffset.set(-width / 2.0f, -height / 2.0f);
    }

    public void endFrame() {
        pressedButtons.clear();
        pressedKeys.clear();
        consumedDrag.clear();
        prevCursorPos.set(cursorPos);
        scroll.set(0, 0);
        textInput = "";
    }

    public void onEvent(EventDispatcher d) {
        d.dispatch(MouseButtonPressEvent.class, this::onMouseButtonPress);
        d.dispatch(MouseButtonReleaseEvent.class, this::onMouseButtonRelease);
        d.dispatch(MouseScrollEvent.class, this::onMouseScroll);
        d.dispatch(MouseMoveEvent.class, this::onMouse);

        d.dispatch(KeyPressEvent.class, this::onKeyPressOrRepeat);
        d.dispatch(KeyRepeatEvent.class, this::onKeyPressOrRepeat);
        d.dispatch(KeyReleaseEvent.class, this::onKeyRelease);
        d.dispatch(CharTypeEvent.class, this::onCharType);
    }
}
