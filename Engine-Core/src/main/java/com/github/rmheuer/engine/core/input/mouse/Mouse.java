package com.github.rmheuer.engine.core.input.mouse;

import com.github.rmheuer.engine.core.input.InputSource;
import com.github.rmheuer.engine.core.math.Vector2f;

public interface Mouse extends InputSource {
    boolean isButtonPressed(MouseButton button);

    Vector2f getCursorPos();
    
    default float getCursorX() {
	return getCursorPos().x;
    }
    
    default float getCursorY() {
	return getCursorPos().y;
    }
}
