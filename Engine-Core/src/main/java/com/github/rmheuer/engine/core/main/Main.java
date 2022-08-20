package com.github.rmheuer.engine.core.main;

public final class Main {
    public static void main(String[] args) {
        Game.get().run();
    }

    private Main() {
        throw new AssertionError();
    }
}
