package com.github.rmheuer.engine.audio.source;

import com.github.rmheuer.engine.audio.AudioFormat;

import java.nio.ShortBuffer;
import java.io.IOException;

public interface SampleSource {    
    // The buffer returned here should be freed with MemoryUtil.memFree(buffer)
    ShortBuffer readAllSamples() throws IOException;

    SampleStream streamSamples() throws IOException;

    // Only guaranteed to be valid after reading samples
    AudioFormat getFormat();
    int getSampleRate();
}
