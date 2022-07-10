package com.github.rmheuer.engine.audio.component;

import com.github.rmheuer.engine.core.ecs.component.Component;

public final class AudioListener implements Component {
    private float gain;

    public AudioListener() {
	gain = 1.0f;	
    }

    public AudioListener(float gain) {
	this.gain = gain;
    }

    public float getGain() {
	return gain;
    }

    public void setGain(float gain) {
	this.gain = gain;
    }
}
