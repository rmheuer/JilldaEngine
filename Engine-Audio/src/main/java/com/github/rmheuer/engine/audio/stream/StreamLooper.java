package com.github.rmheuer.engine.audio.stream;

import com.github.rmheuer.engine.audio.NativeAudioBuffer;
import com.github.rmheuer.engine.core.resource.ResourceFile;

public final class StreamLooper implements AutoCloseable {
    private final ResourceFile file;
    private PCMSampleStream current;
    private boolean enabled;

    public StreamLooper(ResourceFile file, boolean enabled) {
        this.file = file;
        current = PCMSampleStream.open(file);
        this.enabled = enabled;
    }

    public boolean fillStreamingBuffer(NativeAudioBuffer buffer) {
        if (current.fillStreamingBuffer(buffer))
            return true;

        if (!enabled) return false;

        // Re-open the stream
        try {
            current.close();
        } catch (Exception e) {
            System.err.println("Failed to close stream:");
            e.printStackTrace();
        }
        current = PCMSampleStream.open(file); // TODO: Maybe reset stream instead of re-open?

        return current.fillStreamingBuffer(buffer);
    }

    @Override
    public void close() throws Exception {
        current.close();
    }

    public boolean hasReachedEnd() {
        return !enabled && current.hasReachedEnd();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
