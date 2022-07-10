package com.github.rmheuer.engine.audio.openal;

import com.github.rmheuer.engine.audio.AudioBuffer;
import com.github.rmheuer.engine.audio.AudioFormat;

import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL11.*;

public final class OpenALBuffer implements AudioBuffer {
    private final int id;

    public OpenALBuffer() {
	id = alGenBuffers();
    }

    @Override
    public void putData(AudioFormat format, short[] data, int sampleRate) {
	alBufferData(id, alFormat(format), data, sampleRate);
    }

    @Override
    public void putData(AudioFormat format, ShortBuffer data, int sampleRate) {
	alBufferData(id, alFormat(format), data, sampleRate);
    }

    private int alFormat(AudioFormat format) {
	if (format == AudioFormat.MONO) {
	    return AL_FORMAT_MONO16;
	} else {
	    return AL_FORMAT_STEREO16;
	}
    }

    int getId() {
	return id;
    }

    @Override
    public void delete() {
	alDeleteBuffers(id);
    }
}
