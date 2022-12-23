package com.github.rmheuer.engine.audio;

import java.nio.ShortBuffer;

public interface NativeAudioBuffer {
    void setData(AudioDataFormat format, ShortBuffer pcm, int sampleRate);

    void delete();
}
