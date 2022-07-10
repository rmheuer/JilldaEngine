package com.github.rmheuer.engine.audio.event;

import com.github.rmheuer.engine.audio.component.AudioSource;

public final class SourcePauseEvent extends SourceEvent {
    public SourcePauseEvent(AudioSource source) {
	super(source);
    }
}
