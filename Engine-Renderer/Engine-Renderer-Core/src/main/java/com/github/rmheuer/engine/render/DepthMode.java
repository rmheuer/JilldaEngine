package com.github.rmheuer.engine.render;

public enum DepthMode {
    DISABLED(false, false),
    TEST_ONLY(true, false),
    WRITE_ONLY(false, true),
    TEST_AND_WRITE(true, true);

    private final boolean test, write;

    DepthMode(boolean test, boolean write) {
        this.test = test;
        this.write = write;
    }

    public boolean isTest() {
        return test;
    }

    public boolean isWrite() {
        return write;
    }
}
