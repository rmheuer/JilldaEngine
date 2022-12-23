package com.github.rmheuer.engine.audio;

import com.github.rmheuer.engine.core.math.Vector3f;

public interface NativeAudioSource {
    void setGlobalPosition(Vector3f pos);

    void setListenerRelativePosition(Vector3f pos);

    void setVelocity(Vector3f vel);

    void setGain(float gain);

    void setPitch(float pitch);

    /**
     * Adds a buffer to the streaming queue.
     *
     * @param buffer buffer to stream
     */
    void queueStreamingBuffer(NativeAudioBuffer buffer);

    /**
     * Gets any buffer that has finished playing.
     *
     * @return finished buffer if available, else null
     */
    NativeAudioBuffer pollProcessedStreamBuffer();

    void play();

    void pause();

    void stop();

    void release();

    boolean isPlaying();
}
