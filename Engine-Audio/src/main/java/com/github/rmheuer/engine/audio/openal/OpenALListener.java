package com.github.rmheuer.engine.audio.openal;

import com.github.rmheuer.engine.audio.NativeAudioListener;
import com.github.rmheuer.engine.core.math.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.openal.AL10.*;

public final class OpenALListener implements NativeAudioListener {
    private static final OpenALListener INSTANCE = new OpenALListener();

    public static OpenALListener get() {
        return INSTANCE;
    }

    private OpenALListener() {}

    @Override
    public void setPosition(Vector3f pos) {
        alListener3f(AL_POSITION, pos.x, pos.y, pos.z);
    }

    @Override
    public void setOrientation(Vector3f forward, Vector3f up) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buf = stack.floats(
                    forward.x, forward.y, forward.z,
                    up.x, up.y, up.z
            );
            alListenerfv(AL_ORIENTATION, buf);
        }
    }

    @Override
    public void setVelocity(Vector3f vel) {
        alListener3f(AL_VELOCITY, vel.x, vel.y, vel.z);
    }

    @Override
    public void setGain(float gain) {
        if (gain < 0)
            throw new IllegalArgumentException("Gain must be at least zero");
        alListenerf(AL_GAIN, gain);
    }
}
