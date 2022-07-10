package com.github.rmheuer.engine.gui;

@FunctionalInterface
public interface TextFilter {
    boolean passes(String str);
}
