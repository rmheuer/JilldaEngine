package com.github.rmheuer.engine.audio;

import java.nio.ShortBuffer;

public interface AudioBuffer {
    void putData(AudioFormat format, short[] data, int sampleRate);
    void putData(AudioFormat format, ShortBuffer data, int sampleRate);

    void delete();
}
