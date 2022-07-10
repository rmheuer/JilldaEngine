package com.github.rmheuer.engine.audio.source;

import com.github.rmheuer.engine.audio.AudioFormat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class OggVorbisSource implements SampleSource {
    private final ResourceFile res;

    private AudioFormat format;
    private int sampleRate;

    public OggVorbisSource(ResourceFile res) {
	this.res = res;
    }
    
    @Override
    public ShortBuffer readAllSamples() throws IOException {
	ByteBuffer data = res.readAsDirectByteBuffer();

	try (MemoryStack stack = MemoryStack.stackPush()) {
	    IntBuffer error = stack.mallocInt(1);
	    long vb = stb_vorbis_open_memory(data, error, null);
	    if (vb == NULL) {
		throw new IOException("Failed to open file (Error " + error.get(0) + ")");
	    }

	    STBVorbisInfo info = STBVorbisInfo.malloc(stack);
	    stb_vorbis_get_info(vb, info);

	    int channels = info.channels();
	    int length = stb_vorbis_stream_length_in_samples(vb);

	    if (channels == 1) {
		format = AudioFormat.MONO;
	    } else {
		format = AudioFormat.STEREO;
	    }
	    sampleRate = info.sample_rate();

	    ShortBuffer pcm = MemoryUtil.memAllocShort(length);
	    pcm.limit(stb_vorbis_get_samples_short_interleaved(vb, channels, pcm) * channels);
	    
	    stb_vorbis_close(vb);

	    return pcm;
	}
    }

    @Override
    public SampleStream streamSamples() throws IOException {
	throw new RuntimeException("Not implemented");
    }

    @Override
    public AudioFormat getFormat() {
	return format;
    }

    @Override
    public int getSampleRate() {
	return sampleRate;
    }
}
