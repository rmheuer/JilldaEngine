package com.github.rmheuer.engine.audio.stream;

import com.github.rmheuer.engine.audio.AudioDataFormat;
import com.github.rmheuer.engine.audio.NativeAudioBuffer;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import org.lwjgl.system.MemoryUtil;

import java.nio.ShortBuffer;

public abstract class PCMSampleStream implements AutoCloseable {
    public static PCMSampleStream open(ResourceFile res) {
        String name = res.getName().toLowerCase();
        String extension = name.substring(name.lastIndexOf('.') + 1);
        switch (extension) {
            case "ogg": return new OggVorbisStream(res);
            default: throw new RuntimeException("Unsupported audio format: " + extension);
        }
    }

    protected int sampleRate;
    protected AudioDataFormat format;

    private boolean reachedEnd;

    protected int bufferSize;

    protected void init(int sampleRate, AudioDataFormat format) {
        this.sampleRate = sampleRate;
        this.format = format;

        // Each streaming buffer contains a second of audio data
        bufferSize = sampleRate * format.getChannelCount();

        reachedEnd = false;
    }

    // Should return ShortBuffer with max size bufferSize, allocated with MemoryUtil.memAllocShort
    // Return null to indicate end of stream
    protected abstract ShortBuffer readSamples();

    public boolean fillStreamingBuffer(NativeAudioBuffer buffer) {
        ShortBuffer data = readSamples();
        if (data == null) {
            reachedEnd = true;
            return false;
        }

        buffer.setData(format, data, sampleRate);

        MemoryUtil.memFree(data);
        return true;
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }

    @Override
    public void close() throws Exception {}
}
