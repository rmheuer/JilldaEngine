package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.AudioBackend;
import com.github.rmheuer.engine.audio.openal.OpenALBackend;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.module.GameModule;

public final class AudioModule implements GameModule {
    private AudioBackend backend;

    @Override
    public void initializeWorld(Game.WorldBuilder builder) {
        builder
                .addSystem(AudioUpdateListenerSystem.class)
                .addSystem(AudioUpdateSourcePositionSystem.class)
                .addSystem(AudioQueueStreamingBuffersSystem.class)
                .addSystem(AudioSourceLifetimeSystem.class)
                .addSystem(AudioSourceUpdatePropertiesSystem.class)
                .addSystem(AudioBeginPlaySystem.class);
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
