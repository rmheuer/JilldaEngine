package com.github.rmheuer.engine.audio;

import java.nio.ByteBuffer;

public interface NativeAudioBuffer {
    void setData(AudioDataFormat format, ByteBuffer data, int sampleRate);

    void delete();
}
