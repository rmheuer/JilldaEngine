package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.AudioBackend;
import com.github.rmheuer.engine.audio.openal.OpenALBackend;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.module.GameModule;

import java.util.Arrays;
import java.util.Collection;

public final class AudioModule implements GameModule {
    private AudioBackend backend;

    @Override
    public Collection<Class<? extends GameSystem>> getSystems() {
        return Arrays.asList(
                AudioUpdateListenerSystem.class,
                AudioUpdateSourcePositionSystem.class,
                AudioQueueStreamingBuffersSystem.class,
                AudioSourceLifetimeSystem.class,
                AudioSourceUpdatePropertiesSystem.class
        );
    }

    @Override
    public void init() {
        backend = new OpenALBackend();
    }

    @Override
    public void close() {
        backend.dispose();
    }
}
