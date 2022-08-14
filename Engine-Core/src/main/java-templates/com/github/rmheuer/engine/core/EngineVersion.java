package com.github.rmheuer.engine.core;

public final class EngineVersion {
    public static final String VERSION = "${project.version}";
    
    private EngineVersion() {
        throw new AssertionError();
    }
}
