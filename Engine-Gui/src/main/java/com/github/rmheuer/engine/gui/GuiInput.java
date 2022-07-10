package com.github.rmheuer.engine.gui;

import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.input.keyboard.CharTypeEvent;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.keyboard.KeyPressEvent;
import com.github.rmheuer.engine.core.input.keyboard.KeyReleaseEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.input.mouse.MouseButtonPressEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseButtonReleaseEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseMoveEvent;
import com.github.rmheuer.engine.core.input.mouse.MouseScrollEvent;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.render2d.Rectangle;

import java.util.EnumSet;
import java.util.Set;

// TODO: Properly take camera transform and projection into account when finding cursor pos
public final class GuiInput {
    private final Set<MouseButton> pressedButtons;
    private final Set<MouseButton> heldButtons;
    private final Set<MouseButton> consumedDrag;
    private final Set<Key> pressedKeys;
    private final Set<Key> heldKeys;
    private Vector2f cursorOffset;
    private Vector2f prevCursorPos;
    private Vector2f cursorPos;
    private Vector2f scroll;
    private String textInput;

    public GuiInput() {
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
    }

    public boolean isMouseInRect(Rectangle rect) {
        return rect.containsPoint(cursorPos);
    }

    public boolean isMouseButtonPressed(MouseButton button) {
        return pressedButtons.contains(button);
    }

    public boolean isMouseButtonHeld(MouseButton button) {
        return heldButtons.contains(button);
    }

    public boolean isKeyPressed(Key key) {
        return pressedKeys.contains(key);
    }

    public boolean isShiftHeld() {
        return heldKeys.contains(Key.LEFT_SHIFT) || heldKeys.contains(Key.RIGHT_SHIFT);
    }

    public String getTextInput() {
        return textInput;
    }

    public void consumeTextInput() {
        textInput = "";
    }

    public Vector2f getDrag(MouseButton button) {
        if (consumedDrag.contains(button))
            return new Vector2f(0, 0);

        if (!heldButtons.contains(button))
            return new Vector2f(0, 0);

        return new Vector2f(cursorPos).sub(prevCursorPos);
    }

    public Vector2f getDragInRect(Rectangle rect, MouseButton button) {
        if (rect.containsPoint(prevCursorPos)) {
            return getDrag(button);
        }

        return new Vector2f(0, 0);
    }

    public Vector2f getScrollInRect(Rectangle rect) {
        if (!rect.containsPoint(cursorPos))
            return new Vector2f(0, 0);

        return scroll;
    }

    public void consumeDrag(MouseButton button) {
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
        scroll.add(event.getScrollX(), event.getScrollY());
        System.out.println(event);
        onMouse(event);
    }

    private void onMouse(MouseEvent event) {
        cursorPos.set(event.getX(), event.getY()).add(cursorOffset);
        System.out.println(cursorPos);
    }

    private void onKeyPress(KeyPressEvent event) {
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

        d.dispatch(KeyPressEvent.class, this::onKeyPress);
        d.dispatch(KeyReleaseEvent.class, this::onKeyRelease);
        d.dispatch(CharTypeEvent.class, this::onCharType);
    }
}
