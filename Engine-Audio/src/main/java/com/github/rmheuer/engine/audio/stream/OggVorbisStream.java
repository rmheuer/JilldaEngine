package com.github.rmheuer.engine.audio.stream;

import com.github.rmheuer.engine.audio.AudioDataFormat;
import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import org.lwjgl.PointerBuffer;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_close;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class OggVorbisStream extends PCMSampleStream {
    private static final int BUFFER_SIZE_INCREMENT = 1024;

    private final InputStream in;
    private final long vb;
    private final Queue<ShortBuffer> chunkQueue;
    private ShortBuffer currentFillingBuffer;

    private ByteBuffer buffer;
    private boolean hitEOF;

    public OggVorbisStream(ResourceFile file) {
        chunkQueue = new ArrayDeque<>();

        try {
            in = file.readAsStream();
            buffer = MemoryUtil.memAlloc(BUFFER_SIZE_INCREMENT);
            buffer.position(0);
            buffer.limit(0);
            readMoreData(); // Initial buffer read

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer bytesConsumed = stack.mallocInt(1);
                IntBuffer error = stack.mallocInt(1);

                long vb;
                while (true) {
                    vb = stb_vorbis_open_pushdata(buffer, bytesConsumed, error, null);
                    if (vb != NULL) {
                        buffer.position(buffer.position() + bytesConsumed.get(0)); // Advance position
                        break; // Successfully read header, we can start streaming
                    }

                    int err = error.get(0);
                    if (err != VORBIS_need_more_data) {
                        throw new IOException("Failed to decode OGG Vorbis header: " + vorbisErrToString(err));
                    }

                    if (!readMoreData())
                        throw new IOException("EOF while reading OGG Vorbis header");
                }
                this.vb = vb;

                STBVorbisInfo info = STBVorbisInfo.malloc(stack);
                stb_vorbis_get_info(vb, info);

                int sampleRate = info.sample_rate();
                AudioDataFormat format = AudioDataFormat.fromChannelCount(info.channels());

                init(sampleRate, format);

                currentFillingBuffer = MemoryUtil.memAllocShort(bufferSize);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to open OGG stream", e);
        }

        hitEOF = false;
    }

    // If position is at zero, makes a bigger buffer
    // Otherwise, moves the data to the start of the buffer
    private void makeMoreSpace() {
        boolean grow = buffer.position() == 0;

        ByteBuffer newBuffer = MemoryUtil.memAlloc(buffer.capacity() + (grow ? BUFFER_SIZE_INCREMENT : 0));
        newBuffer.put(buffer);
        MemoryUtil.memFree(buffer);
        newBuffer.flip();
        buffer = newBuffer;
    }

    private boolean readMoreData() throws IOException {
        // Make sure we have more space
        int remaining;
        while ((remaining = buffer.capacity() - buffer.limit()) == 0)
            makeMoreSpace();

        // Read in some data
        byte[] data = new byte[remaining];
        int read = in.read(data);
        if (read < 0)
            return false; // Return false if EOF

        // Buffer the data
        int positionBackup = buffer.position();
        int limit = buffer.limit();
        buffer.position(limit);
        buffer.limit(limit + read);
        buffer.put(data, 0, read);
        buffer.position(positionBackup);

        return true;
    }

    private short convertSampleFloatToShort(float sample) {
        return (short) MathUtils.clamp((int) (sample * 32767.5f - 0.5f), -32768, 32767);
    }

    @Override
    protected ShortBuffer readSamples() {
        if (hitEOF && chunkQueue.isEmpty() && currentFillingBuffer == null)
            return null;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pSampleCount = stack.mallocInt(1);
            IntBuffer pChannelCount = stack.mallocInt(1);
            PointerBuffer pppOutput = stack.mallocPointer(1);

            enqueue: while (currentFillingBuffer != null && chunkQueue.isEmpty()) {
                while (true) {
                    int bytesUsed = stb_vorbis_decode_frame_pushdata(vb, buffer, pChannelCount, pppOutput, pSampleCount);
                    int err = stb_vorbis_get_error(vb);
                    if (err != VORBIS__no_error && err != VORBIS_need_more_data) {
                        throw new IOException("Failed to read OGG Vorbis frame: " + vorbisErrToString(err));
                    }

                    if (bytesUsed != 0) {
                        buffer.position(buffer.position() + bytesUsed);
                        break;
                    }

                    if (!readMoreData()) {
                        hitEOF = true;
                        break enqueue; // Reached end of stream
                    }
                }

                int sampleCount = pSampleCount.get(0);
                int channelCount = pChannelCount.get(0);

                if (channelCount == 0)
                    continue;

                PointerBuffer ppOutput = pppOutput.getPointerBuffer(0, channelCount);
                FloatBuffer[] channelData = new FloatBuffer[channelCount];
                for (int channel = 0; channel < channelCount; channel++) {
                    channelData[channel] = ppOutput.getFloatBuffer(channel, sampleCount);
                }

                for (int i = 0; i < sampleCount; i++) {
                    for (FloatBuffer channel : channelData) {
                        currentFillingBuffer.put(convertSampleFloatToShort(channel.get(i)));
                    }
                    if (!currentFillingBuffer.hasRemaining()) {
                        currentFillingBuffer.flip();
                        chunkQueue.add(currentFillingBuffer);
                        currentFillingBuffer = MemoryUtil.memAllocShort(bufferSize);
                    }
                }
            }

            // We have some leftover data
            if (hitEOF && currentFillingBuffer != null && currentFillingBuffer.position() != 0) {
                currentFillingBuffer.flip();
                chunkQueue.add(currentFillingBuffer);
                currentFillingBuffer = null;
            }
        } catch (IOException e) {
            System.err.println("Error streaming OGG Vorbis:");
            e.printStackTrace();
            return null;
        }

        return chunkQueue.remove();
    }

    @Override
    public void close() throws Exception {
        stb_vorbis_close(vb);

        in.close();
        MemoryUtil.memFree(buffer);

        // Free non-consumed buffers if the stream was interrupted
        if (currentFillingBuffer != null)
            MemoryUtil.memFree(currentFillingBuffer);
        for (ShortBuffer buf : chunkQueue)
            MemoryUtil.memFree(buf);
    }

    private static String vorbisErrToString(int err) {
        switch (err) {
            case VORBIS__no_error: return "No error";
            case VORBIS_bad_packet_type: return "Bad packet type";
            case VORBIS_cant_find_last_page: return "Can't find last page";
            case VORBIS_continued_packet_flag_invalid: return "Continued packet flag invalid";
            case VORBIS_feature_not_supported: return "Feature not supported";
            case VORBIS_file_open_failure: return "File open failure";
            case VORBIS_incorrect_stream_serial_number: return "Incorrect stream serial number";
            case VORBIS_invalid_api_mixing: return "Invalid API mixing";
            case VORBIS_invalid_first_page: return "Invalid first page";
            case VORBIS_invalid_setup: return "Invalid setup";
            case VORBIS_invalid_stream: return "Invalid stream";
            case VORBIS_invalid_stream_structure_version: return "Invalid stream structure version";
            case VORBIS_missing_capture_pattern: return "Missing capture pattern";
            case VORBIS_ogg_skeleton_not_supported: return "OGG skeleton not supported";
            case VORBIS_outofmem: return "Out of memory";
            case VORBIS_seek_failed: return "Seek failed";
            case VORBIS_seek_invalid: return "Seek invalid";
            case VORBIS_seek_without_length: return "Seek without length";
            case VORBIS_too_many_channels: return "Too many channels";
            case VORBIS_unexpected_eof: return "Unexpected EOF";
            default: return "Unknown error (" + err + ")";
        }
    }
}
