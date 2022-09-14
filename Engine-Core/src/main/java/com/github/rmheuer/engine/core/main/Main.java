package com.github.rmheuer.engine.core.main;

public final class Main {
    public static void main(String[] args) {
        Game.get().run(args);
    }

    private Main() {
        throw new AssertionError();
    }
}
