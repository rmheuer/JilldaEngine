package com.github.rmheuer.engine.audio;

import com.github.rmheuer.engine.core.math.Vector3f;

public interface Listener {
    void setGain(float gain);
    void setPosition(Vector3f position);
    void setOrientation(Vector3f at, Vector3f up);
}
