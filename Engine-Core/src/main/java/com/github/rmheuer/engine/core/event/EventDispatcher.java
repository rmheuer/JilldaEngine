package com.github.rmheuer.engine.core.event;

import java.util.function.Consumer;

public final class EventDispatcher {
    private final Event event;

    public EventDispatcher(Event event) {
	this.event = event;
    }

    public <T extends Event> void dispatch(Class<T> type, Consumer<T> fn) {
	if (type.isInstance(event)) {
	    fn.accept(type.cast(event));
	}
    }

    public Event getEvent() {
	return event;
    }
}

