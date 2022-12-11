package com.github.rmheuer.engine.core.main;

import static org.lwjgl.system.Configuration.*;

public final class Main {
    public static void main(String[] args) {
        if (System.getProperty("engine.debug.lwjgl") != null) {
            DEBUG.set(true);
            DEBUG_FUNCTIONS.set(true);
            DEBUG_LOADER.set(true);
            DEBUG_MEMORY_ALLOCATOR.set(true);
            DEBUG_STACK.set(true);
            DEBUG_STREAM.set(true);
        }

        Game.get().run(args);
    }

    private Main() {
        throw new AssertionError();
    }
}
