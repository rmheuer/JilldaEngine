package com.github.rmheuer.engine.audio.event;

import com.github.rmheuer.engine.audio.component.AudioSource;

public final class SourceResumeEvent extends SourceEvent {
    public SourceResumeEvent(AudioSource source) {
	super(source);
    }
}
