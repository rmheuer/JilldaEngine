package com.github.rmheuer.engine.audio.openal;

import com.github.rmheuer.engine.audio.AudioBackend;
import com.github.rmheuer.engine.audio.AudioBuffer;
import com.github.rmheuer.engine.audio.Listener;
import com.github.rmheuer.engine.audio.NativeAudioSource;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class OpenALBackend implements AudioBackend {
    private static final int MAX_SOURCES = 255;

    private final long device;
    private final long context;
    private final List<OpenALSource> sourcePool;
    
    public OpenALBackend() {
	device = alcOpenDevice((ByteBuffer) null);
	if (device == NULL) {
	    throw new RuntimeException("Failed to open default OpenAL device");
	}

	ALCCapabilities caps = ALC.createCapabilities(device);
	context = alcCreateContext(device, (IntBuffer) null);
	if (context == NULL) {
	    throw new RuntimeException("Failed to create OpenAL context");
	}
	alcMakeContextCurrent(context);
	AL.createCapabilities(caps);
	
	sourcePool = new LinkedList<>();
	for (int i = 0; i < MAX_SOURCES; i++) {
	    sourcePool.add(new OpenALSource(this));
	}
    }

    @Override
    public Listener getListener() {
	return OpenALListener.get();
    }

    @Override
    public NativeAudioSource requestNativeSource() {
	if (sourcePool.isEmpty()) {
	    System.err.println("No audio sources available in pool");
	    return null;
	}
	return sourcePool.remove(0);
    }

    void releaseSource(OpenALSource source) {
	sourcePool.add(source);
	if (sourcePool.size() > MAX_SOURCES) {
	    throw new IllegalStateException("Sources released more than requested");
	}
    }

    @Override
    public AudioBuffer createBuffer() {
	return new OpenALBuffer();
    }

    @Override
    public void deleteContext() {
	if (sourcePool.size() != MAX_SOURCES) {
	    throw new IllegalStateException("Not all audio sources released");
	}

	for (OpenALSource source : sourcePool) {
	    source.delete();
	}
    }
}
