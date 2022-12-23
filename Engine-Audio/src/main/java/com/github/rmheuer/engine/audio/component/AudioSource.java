package com.github.rmheuer.engine.audio.component;

import com.github.rmheuer.engine.audio.NativeAudioBuffer;
import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.audio.stream.StreamLooper;
import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.resource.ResourceFile;

import java.util.Collection;

public final class AudioSource implements Component {
    public enum Mode {
        SOURCE_2D,
        SOURCE_3D
    }

    private ResourceFile file;
    private Mode mode;
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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
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
        private boolean playStarted;

        public Runtime(NativeAudioSource source, Collection<NativeAudioBuffer> ownedBuffers, StreamLooper looper) {
            this.source = source;
            this.ownedBuffers = ownedBuffers;
            this.looper = looper;
            playStarted = false;
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

        public boolean isPlayStarted() {
            return playStarted;
        }

        public void setPlayStarted(boolean playStarted) {
            this.playStarted = playStarted;
        }
    }
}
