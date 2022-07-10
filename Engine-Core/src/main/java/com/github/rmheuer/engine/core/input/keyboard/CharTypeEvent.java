package com.github.rmheuer.engine.core.input.keyboard;

public final class CharTypeEvent implements KeyboardEvent {
    private final char c;

    public CharTypeEvent(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }

    @Override
    public String toString() {
        return "CharTypeEvent{" +
                "char=" + c +
                '}';
    }
}
