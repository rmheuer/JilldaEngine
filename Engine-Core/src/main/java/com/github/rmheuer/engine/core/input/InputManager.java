package com.github.rmheuer.engine.core.input;

import java.util.HashMap;
import java.util.Map;

public final class InputManager {
    private final Map<Class<? extends InputSource>, InputSource> sources;

    public InputManager() {
	sources = new HashMap<>();
    }

    public <T extends InputSource> void registerSource(Class<T> type, T source) {
	if (sources.containsKey(type)) {
	    throw new IllegalStateException("Input source already registered of type " + type.getName());
	}

	sources.put(type, source);
    }

    public <T extends InputSource> T getSource(Class<T> type) {
	InputSource source = sources.get(type);

	@SuppressWarnings("unchecked")
	T t = (T) source;

	return t;
    }
}
