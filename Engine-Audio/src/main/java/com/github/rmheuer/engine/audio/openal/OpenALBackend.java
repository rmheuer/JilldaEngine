package com.github.rmheuer.engine.audio.openal;

import com.github.rmheuer.engine.audio.AudioBackend;
import com.github.rmheuer.engine.audio.NativeAudioBuffer;
import com.github.rmheuer.engine.audio.NativeAudioListener;
import com.github.rmheuer.engine.audio.NativeAudioSource;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.ALC10.*;

public final class OpenALBackend extends AudioBackend {
    private static final int MAX_SOURCES = 255;

    private final long device;
    private final long context;
    private final List<OpenALSource> sourcePool;

    public OpenALBackend() {
        device = alcOpenDevice((ByteBuffer) null);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        context = alcCreateContext(device, (IntBuffer) null);
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);

        sourcePool = new ArrayList<>();
        for (int i = 0; i < MAX_SOURCES; i++) {
            sourcePool.add(new OpenALSource(this));
        }
    }

    @Override
    public NativeAudioListener getListener() {
        return OpenALListener.get();
    }

    @Override
    public NativeAudioSource requestSource() {
        if (sourcePool.isEmpty()) {
            System.err.println("OpenAL: Source pool is empty");
            return null;
        }

        return sourcePool.remove(0);
    }

    @Override
    public NativeAudioBuffer createBuffer() {
        return new OpenALBuffer();
    }

    @Override
    public void dispose() {
        if (sourcePool.size() != MAX_SOURCES)
            throw new RuntimeException("OpenAL: Not all sources returned to the pool");

        for (OpenALSource source : sourcePool) {
            source.delete();
        }

        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public void returnSourceToPool(OpenALSource source) {
        sourcePool.add(source);
    }
}
