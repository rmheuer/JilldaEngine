package com.github.rmheuer.engine.audio.openal;

import com.github.rmheuer.engine.audio.AudioDataFormat;
import com.github.rmheuer.engine.audio.NativeAudioBuffer;

import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

public final class OpenALBuffer implements NativeAudioBuffer {
    final int id;

    public OpenALBuffer() {
        id = alGenBuffers();
    }

    private int getAlAudioDataFormat(AudioDataFormat fmt) {
        switch (fmt) {
            case MONO: return AL_FORMAT_MONO16;
            case STEREO: return AL_FORMAT_STEREO16;
            default:
                throw new IllegalArgumentException(String.valueOf(fmt));
        }
    }

    @Override
    public void setData(AudioDataFormat format, ShortBuffer data, int sampleRate) {
        alBufferData(id, getAlAudioDataFormat(format), data, sampleRate);
    }

    @Override
    public void delete() {
        alDeleteBuffers(id);
    }
}
