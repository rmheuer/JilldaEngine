package com.github.rmheuer.engine.audio;

import com.github.rmheuer.engine.core.math.Vector3f;

public interface NativeAudioSource {
    void setPosition(Vector3f pos);

    void attachBuffer(AudioBuffer buffer);
    void detachBuffers();

    void queueBuffer(AudioBuffer buffer);
    AudioBuffer unqueueBuffer();
    int getProcessedBuffers();
    
    void setPitch(float pitch);
    void setGain(float gain);

    boolean isPlaying();
    void play();
    void pause();
    void stop();

    void setLooping(boolean looping);

    void release();
}
