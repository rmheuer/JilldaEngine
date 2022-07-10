package com.github.rmheuer.engine.audio.event;

import com.github.rmheuer.engine.audio.component.AudioSource;

public final class SourcePlayEvent extends SourceEvent {
    public SourcePlayEvent(AudioSource source) {
	super(source);
    }
}
