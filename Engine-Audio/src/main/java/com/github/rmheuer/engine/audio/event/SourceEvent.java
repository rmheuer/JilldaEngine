package com.github.rmheuer.engine.audio.event;

import com.github.rmheuer.engine.audio.component.AudioSource;
import com.github.rmheuer.engine.core.event.Event;

public abstract class SourceEvent implements Event {
    private final AudioSource source;

    public SourceEvent(AudioSource source) {
	this.source = source;
    }

    public AudioSource getSource() {
	return source;
    }

    @Override
    public String toString() {
	return getClass().getSimpleName() + "{"
	    + "source=" + source
	    + "}";
    }
}
