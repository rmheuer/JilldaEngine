package com.github.rmheuer.engine.audio;

public enum AudioDataFormat {
    MONO(1),
    STEREO(2);

    public static AudioDataFormat fromChannelCount(int channels) {
        switch (channels) {
            case 1: return MONO;
            case 2: return STEREO;
            default:
                throw new RuntimeException("Unsupported audio format (" + channels + " channels)");
        }
    }

    private final int channelCount;

    AudioDataFormat(int channelCount) {
        this.channelCount = channelCount;
    }

    public int getChannelCount() {
        return channelCount;
    }
}
