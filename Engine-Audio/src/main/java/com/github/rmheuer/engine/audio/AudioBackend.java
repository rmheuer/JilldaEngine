package com.github.rmheuer.engine.audio;

public interface AudioBackend {
    /**
     * Gets the one listener instance for this backend.
     *
     * @return listener instance
     */
    NativeAudioListener getListener();

    /**
     * Attempts to get an available source. It is possible for this
     * method to return {@code null} if no audio sources are
     * currently available.
     */
    NativeAudioSource requestSource();

    NativeAudioBuffer createBuffer();
}
