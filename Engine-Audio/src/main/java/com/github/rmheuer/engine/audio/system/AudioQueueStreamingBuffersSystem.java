package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.NativeAudioBuffer;
import com.github.rmheuer.engine.audio.component.AudioSource;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;

public final class AudioQueueStreamingBuffersSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
        world.forEach(AudioSource.class, (source) -> {
            AudioSource.Runtime runtime = source.getRuntime();
            if (runtime == null)
                return;

            // Don't try to stream more if the end is reached
            if (runtime.getLooper().hasReachedEnd())
                return;

            // Fill buffers
            NativeAudioBuffer buffer;
            while ((buffer = runtime.getSource().pollProcessedStreamBuffer()) != null) {
                if (!runtime.getLooper().fillStreamingBuffer(buffer))
                    break;
                runtime.getSource().queueStreamingBuffer(buffer);
            }
        });
    }
}
