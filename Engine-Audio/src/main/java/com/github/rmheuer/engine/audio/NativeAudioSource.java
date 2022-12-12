package com.github.rmheuer.engine.audio;

import com.github.rmheuer.engine.core.math.Vector3f;

public interface NativeAudioSource {
    void setPosition(Vector3f pos);

    void setVelocity(Vector3f vel);

    void setGain(float gain);

    /**
     * Sets this source to loop the buffer it is playing.
     * This is only applicable when playing a static buffer set
     * using {@link #setStaticBuffer(NativeAudioBuffer)}.
     *
     * @param looping whether to loop
     */
    void setLooping(boolean looping);

    /**
     * Sets this source to play one static buffer. This buffer
     * can be automatically looped using {@link #setLooping(boolean)}.
     *
     * @param buffer buffer to play
     */
    void setStaticBuffer(NativeAudioBuffer buffer);

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
}
