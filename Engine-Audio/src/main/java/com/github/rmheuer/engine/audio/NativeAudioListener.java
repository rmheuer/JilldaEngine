package com.github.rmheuer.engine.audio;

import com.github.rmheuer.engine.core.math.Vector3f;

public interface NativeAudioListener {
    /**
     * Sets the position of this listener in 3D space.
     *
     * @param pos position
     */
    void setPosition(Vector3f pos);

    /**
     * Sets the orientation of this listener in 3D space.
     * The provided vectors do not need to be normalized.
     *
     * @param forward forward direction
     * @param up up direction
     */
    void setOrientation(Vector3f forward, Vector3f up);

    /**
     * Sets the current velocity of this listener. This does not
     * change the position of the listener, it is only used for
     * velocity-dependent effects, such as the Doppler effect.
     *
     * @param vel current velocity
     */
    void setVelocity(Vector3f vel);

    /**
     * Sets the output gain of all sounds heard by the listener.
     *
     * @param gain gain from 0 to 1, where 0 is silence and 1
     *             is no attenuation
     */
    void setGain(float gain);
}
