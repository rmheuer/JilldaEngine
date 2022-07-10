package com.github.rmheuer.engine.audio;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;

public final class AudioContext implements WorldLocalSingleton {
    private AudioBackend backend;

    public AudioContext(AudioBackend backend) {
	this.backend = backend;
    }

    public AudioBackend getBackend() {
	return backend;
    }
}
