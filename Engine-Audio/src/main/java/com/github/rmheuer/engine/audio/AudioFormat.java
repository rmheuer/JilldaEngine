package com.github.rmheuer.engine.audio;

public enum AudioFormat {
    // Mono audio is played in 3D space, relative to the source
    // and listener positions.
    MONO,

    // Stereo audio is played directly to the output regardless
    // of the source position.
    STEREO
}
