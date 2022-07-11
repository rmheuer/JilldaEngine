package com.github.rmheuer.engine.core.resource;

public interface Resource {
    String getName();
    String getPath();
    String getAbsolutePath();

    boolean isFile();
    boolean isGroup();
}
