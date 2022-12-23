package com.github.rmheuer.engine.audio.component;

import com.github.rmheuer.engine.audio.NativeAudioBuffer;
import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.audio.stream.StreamLooper;
import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.resource.ResourceFile;

import java.util.Collection;

public final class AudioSource implements Component {
    private ResourceFile file;
    private boolean looping;
    private float gain = 1.0f;
    private float pitch = 1.0f;

    private Runtime runtime;

    private AudioSource() {}

    public AudioSource(ResourceFile file) {
        this.file = file;
    }

    public ResourceFile getFile() {
        return file;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Runtime getRuntime() {
        return runtime;
    }

    public void setRuntime(Runtime runtime) {
        this.runtime = runtime;
    }

    public static final class Runtime {
        private final NativeAudioSource source;
        private final Collection<NativeAudioBuffer> ownedBuffers;
        private final StreamLooper looper;

        public Runtime(NativeAudioSource source, Collection<NativeAudioBuffer> ownedBuffers, StreamLooper looper) {
            this.source = source;
            this.ownedBuffers = ownedBuffers;
            this.looper = looper;
        }

        public NativeAudioSource getSource() {
            return source;
        }

        public Collection<NativeAudioBuffer> getOwnedBuffers() {
            return ownedBuffers;
        }

        public StreamLooper getLooper() {
            return looper;
        }
    }
}
