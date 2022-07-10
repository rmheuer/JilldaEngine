package com.github.rmheuer.engine.audio.openal;

import com.github.rmheuer.engine.audio.Listener;
import com.github.rmheuer.engine.core.math.Vector3f;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.openal.AL11.*;

public final class OpenALListener implements Listener {
    private static final OpenALListener INSTANCE = new OpenALListener();

    public static OpenALListener get() {
	return INSTANCE;
    }

    private OpenALListener() {}

    @Override
    public void setGain(float gain) {
	alListenerf(AL_GAIN, gain);
    }

    @Override
    public void setPosition(Vector3f position) {
	alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    @Override
    public void setOrientation(Vector3f at, Vector3f up) {
	alListenerfv(AL_ORIENTATION, new float[] {
	    at.x, at.y, at.z,
	    up.x, up.y, up.z
	});
    }
}
