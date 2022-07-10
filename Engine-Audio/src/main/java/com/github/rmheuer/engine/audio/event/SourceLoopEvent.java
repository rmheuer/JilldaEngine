package com.github.rmheuer.engine.audio.event;

import com.github.rmheuer.engine.audio.component.AudioSource;

public final class SourceLoopEvent extends SourceEvent {
    public SourceLoopEvent(AudioSource source) {
	super(source);
    }
}
