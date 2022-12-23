package com.github.rmheuer.engine.audio;

public abstract class AudioBackend {
    private static AudioBackend instance = null;

    public static AudioBackend get() {
        return instance;
    }

    protected AudioBackend() {
        if (instance != null)
            throw new IllegalStateException("Audio backend already instantiated");

        instance = this;
    }

    /**
     * Gets the one listener instance for this backend.
     *
     * @return listener instance
     */
    public abstract NativeAudioListener getListener();

    /**
     * Attempts to get an available source. It is possible for this
     * method to return {@code null} if no audio sources are
     * currently available.
     */
    public abstract NativeAudioSource requestSource();

    public abstract NativeAudioBuffer createBuffer();

    public abstract void dispose();
}
