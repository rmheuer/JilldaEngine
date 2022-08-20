package com.github.rmheuer.engine.core;

public final class Time {
    private static float delta;
    private static float fixedDelta;

    public static float getDelta() {
        return delta;
    }

    public static void setDelta(float delta) {
        Time.delta = delta;
    }

    public static float getFixedDelta() {
        return fixedDelta;
    }

    public static void setFixedDelta(float fixedDelta) {
        Time.fixedDelta = fixedDelta;
    }
}
