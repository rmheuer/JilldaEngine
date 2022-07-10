package com.github.rmheuer.engine.audio.openal;

import com.github.rmheuer.engine.audio.AudioBuffer;
import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.core.math.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.AL11.*;

public final class OpenALSource implements NativeAudioSource {
    private final OpenALBackend backend;
    private final int id;
    private final Map<Integer, AudioBuffer> queuedBuffers;

    public OpenALSource(OpenALBackend backend) {
	this.backend = backend;
	id = alGenSources();
	queuedBuffers = new HashMap<>();
    }

    @Override
    public void setPosition(Vector3f pos) {
	alSource3f(id, AL_POSITION, pos.x, pos.y, pos.z);
    }

    private int bufferId(AudioBuffer buffer) {
	return ((OpenALBuffer) buffer).getId();
    }

    @Override
    public void attachBuffer(AudioBuffer buffer) {
	alSourcei(id, AL_BUFFER, bufferId(buffer));
    }

    @Override
    public void detachBuffers() {
	alSourcei(id, AL_BUFFER, 0);
    }

    @Override
    public void queueBuffer(AudioBuffer buffer) {
	int bufId = bufferId(buffer);
	alSourceQueueBuffers(id, bufId);
	queuedBuffers.put(bufId, buffer);
    }

    @Override
    public AudioBuffer unqueueBuffer() {
	int bufId = alSourceUnqueueBuffers(id);
	return queuedBuffers.remove(bufId);
    }

    @Override
    public int getProcessedBuffers() {
	return alGetSourcei(id, AL_BUFFERS_PROCESSED);
    }

    @Override
    public void setPitch(float pitch) {
	alSourcef(id, AL_PITCH, pitch);
    }

    @Override
    public void setGain(float gain) {
	alSourcef(id, AL_GAIN, gain);
    }

    @Override
    public boolean isPlaying() {
	return alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING;
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
    public void setLooping(boolean looping) {
	alSourcei(id, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
    }

    @Override
    public void release() {
	backend.releaseSource(this);
    }

    void delete() {
	alDeleteSources(id);
    }
}
