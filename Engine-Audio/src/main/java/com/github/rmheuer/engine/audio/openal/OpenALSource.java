package com.github.rmheuer.engine.audio.openal;

import com.github.rmheuer.engine.audio.NativeAudioBuffer;
import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.core.math.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.AL10.*;

public final class OpenALSource implements NativeAudioSource {
    private final OpenALBackend backend;
    private final int id;
    private final Map<Integer, OpenALBuffer> queuedBuffers;

    OpenALSource(OpenALBackend backend) {
        this.backend = backend;
        id = alGenSources();
        queuedBuffers = new HashMap<>();
    }

    @Override
    public void setGlobalPosition(Vector3f pos) {
        alSourcei(id, AL_SOURCE_RELATIVE, AL_FALSE);
        alSource3f(id, AL_POSITION, pos.x, pos.y, pos.z);
    }

    @Override
    public void setListenerRelativePosition(Vector3f pos) {
        alSourcei(id, AL_SOURCE_RELATIVE, AL_TRUE);
        alSource3f(id, AL_POSITION, pos.x, pos.y, pos.z);
    }

    @Override
    public void setVelocity(Vector3f vel) {
        alSource3f(id, AL_VELOCITY, vel.x, vel.y, vel.z);
    }

    @Override
    public void setGain(float gain) {
        alSourcef(id, AL_GAIN, gain);
    }

    @Override
    public void setPitch(float pitch) {
        alSourcef(id, AL_PITCH, pitch);
    }

    @Override
    public void queueStreamingBuffer(NativeAudioBuffer buffer) {
        OpenALBuffer buf = (OpenALBuffer) buffer;
        queuedBuffers.put(buf.id, buf);
        alSourceQueueBuffers(id, buf.id);
    }

    @Override
    public NativeAudioBuffer pollProcessedStreamBuffer() {
        int processed = alGetSourcei(id, AL_BUFFERS_PROCESSED);
        if (processed == 0)
            return null;

        return queuedBuffers.remove(alSourceUnqueueBuffers(id));
    }

    @Override
    public void play() {
        alSourcePlay(id);
    }

    @Override
    public void pause() {
        alSourcePause(id);
    }

    @Override
    public void stop() {
        alSourceStop(id);
    }

    @Override
    public void release() {
        backend.returnSourceToPool(this);
    }

    @Override
    public boolean isPlaying() {
        return alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void delete() {
        alDeleteSources(id);
    }
}
