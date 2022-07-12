package com.github.rmheuer.engine.core;

public final class Time {
    private static float delta;

    public static float getDelta() {
        return delta;
    }

    public static void setDelta(float delta) {
        Time.delta = delta;
    }
}
