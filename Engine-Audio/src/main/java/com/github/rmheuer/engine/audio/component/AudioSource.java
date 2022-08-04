package com.github.rmheuer.engine.audio.component;

import com.github.rmheuer.engine.audio.AudioBuffer;
import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.audio.event.SourcePlayEvent;
import com.github.rmheuer.engine.audio.event.SourceLoopEvent;
import com.github.rmheuer.engine.audio.event.SourcePauseEvent;
import com.github.rmheuer.engine.audio.event.SourceResumeEvent;
import com.github.rmheuer.engine.audio.event.SourceStopEvent;
import com.github.rmheuer.engine.audio.source.SampleSource;
import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.main.Game;

public final class AudioSource implements Component {
    private SampleSource source;
    private float gain;
    private float pitch;
    // TODO: 3D sound toggle
    
    private transient NativeAudioSource nativeSource;
    private transient AudioBuffer buffer;

    public AudioSource() {
	gain = 1.0f;
	pitch = 1.0f;
	source = null;
    }

    public AudioSource(SampleSource source, float gain, float pitch) {
	this.source = source;
	this.gain = gain;
	this.pitch = pitch;
    }

    public void play() {
	Game.get().postGlobalImmediateEvent(new SourcePlayEvent(this));
    }

    public void loop() {
	Game.get().postGlobalImmediateEvent(new SourceLoopEvent(this));
    }

    public void pause() {
	Game.get().postGlobalImmediateEvent(new SourcePauseEvent(this));
    }

    public void resume() {
	Game.get().postGlobalImmediateEvent(new SourceResumeEvent(this));
    }

    public void stop() {
	Game.get().postGlobalImmediateEvent(new SourceStopEvent(this));
    }

    public boolean isPlaying() {
	return nativeSource.isPlaying();
    }

    public SampleSource getSource() {
	return source;
    }

    public void setSource(SampleSource source) {
	this.source = source;
    }

    public float getGain() {
	return gain;
    }

    public void setGain(float gain) {
	this.gain = gain;
    }

    public float getPitch() {
	return pitch;
    }

    public void setPitch(float pitch) {
	this.pitch = pitch;
    }

    public NativeAudioSource getNativeSource() {
	return nativeSource;
    }

    public void setNativeSource(NativeAudioSource nativeSource) {
	this.nativeSource = nativeSource;
    }

    public AudioBuffer getBuffer() {
	return buffer;
    }

    public void setBuffer(AudioBuffer buffer) {
	this.buffer = buffer;
    }
}
