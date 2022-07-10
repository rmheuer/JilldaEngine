package com.github.rmheuer.engine.core.input.keyboard;

import com.github.rmheuer.engine.core.input.InputSource;

public interface Keyboard extends InputSource {
    boolean isKeyPressed(Key key);
}
