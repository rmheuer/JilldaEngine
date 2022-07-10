package com.github.rmheuer.engine.audio.event;

import com.github.rmheuer.engine.audio.component.AudioSource;

public final class SourceStopEvent extends SourceEvent {
    public SourceStopEvent(AudioSource source) {
	super(source);
    }
}
