package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.AudioBackend;
import com.github.rmheuer.engine.audio.NativeAudioBuffer;
import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.audio.component.AudioSource;
import com.github.rmheuer.engine.audio.stream.StreamLooper;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnComponentAdd;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnComponentRemove;

import java.util.ArrayList;
import java.util.List;

public final class AudioSourceLifetimeSystem implements GameSystem {
    private static final int BUFFER_COUNT = 3;

    @OnComponentAdd
    public void onSourceAdd(World world, AudioSource source) {
        if (source.getRuntime() != null)
            throw new IllegalStateException("Audio sources can only be on one entity at a time");

        NativeAudioSource s = AudioBackend.get().requestSource();
        if (s == null)
            return;

        StreamLooper looper = new StreamLooper(source.getFile(), source.isLooping());

        List<NativeAudioBuffer> buffers = new ArrayList<>();
        for (int i = 0; i < BUFFER_COUNT; i++) {
            NativeAudioBuffer buf = AudioBackend.get().createBuffer();
            buffers.add(buf);
            if (!looper.fillStreamingBuffer(buf)) {
                buf.delete();
                break;
            }
            s.queueStreamingBuffer(buf);
        }

        s.setGain(source.getGain());
        s.setPitch(source.getPitch());
        s.play();

        source.setRuntime(new AudioSource.Runtime(s, buffers, looper));
    }

    @OnComponentRemove
    public void onSourceRemove(World world, AudioSource source) {
        AudioSource.Runtime runtime = source.getRuntime();
        if (runtime == null)
            return;

        NativeAudioSource nat = runtime.getSource();
        if (nat.isPlaying())
            nat.stop();

        for (NativeAudioBuffer buf : runtime.getOwnedBuffers())
            buf.delete();
        nat.release();

        closeSourceRuntime(source);
    }

    private void closeSourceRuntime(AudioSource source) {
        try {
            source.getRuntime().getLooper().close();
            source.setRuntime(null);
        } catch (Exception e) {
            System.err.println("Failed to close stream:");
            e.printStackTrace();
        }
    }

    @Override
    public void update(World world, float delta) {
        world.forEachEntity(AudioSource.class, (entity, source) -> {
            AudioSource.Runtime runtime = source.getRuntime();
            if (runtime == null)
                return;

            NativeAudioSource nat = runtime.getSource();
            if (!nat.isPlaying()) {
                // Cleanup will be handled by remove listener
                entity.removeComponent(AudioSource.class);
            }
        });
    }

    @Override
    public void close(World world) {
        world.forEach(AudioSource.class, (source) -> {
            onSourceRemove(world, source);
        });
    }
}
