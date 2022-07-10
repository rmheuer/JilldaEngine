package com.github.rmheuer.engine.core.input.keyboard;

public abstract class KeyEvent implements KeyboardEvent {
    private final Key key;

    public KeyEvent(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{key=" + key + "}";
    }
}
