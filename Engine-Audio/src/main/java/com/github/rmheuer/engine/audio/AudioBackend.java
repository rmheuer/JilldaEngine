package com.github.rmheuer.engine.audio;

public interface AudioBackend {
    Listener getListener();

    // Can return null if no native sources are available
    NativeAudioSource requestNativeSource();

    AudioBuffer createBuffer();

    void deleteContext();
}
