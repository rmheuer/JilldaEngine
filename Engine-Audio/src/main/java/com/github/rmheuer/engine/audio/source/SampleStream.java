package com.github.rmheuer.engine.audio.source;

import java.io.IOException;

public interface SampleStream {
    // Attempt to read as many samples as will fit in the given array.
    // Returns the number of samples read.
    int readSamples(short[] samples) throws IOException;
}
