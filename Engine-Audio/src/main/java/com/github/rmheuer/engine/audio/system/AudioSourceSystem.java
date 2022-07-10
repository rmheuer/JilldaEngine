package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.AudioBuffer;
import com.github.rmheuer.engine.audio.AudioContext;
import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.audio.component.AudioSource;
import com.github.rmheuer.engine.audio.event.SourcePlayEvent;
import com.github.rmheuer.engine.audio.event.SourceLoopEvent;
import com.github.rmheuer.engine.audio.event.SourcePauseEvent;
import com.github.rmheuer.engine.audio.event.SourceResumeEvent;
import com.github.rmheuer.engine.audio.event.SourceStopEvent;
import com.github.rmheuer.engine.audio.source.SampleSource;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.math.Transform;

import java.nio.ShortBuffer;
import java.io.IOException;

public final class AudioSourceSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
	// TODO: Streaming

	world.forEach(AudioSource.class, Transform.class, (source, tx) -> {
	    if (source.getNativeSource() != null) {
		source.getNativeSource().setPosition(tx.getPosition());

		if (!source.isPlaying()) {
		    source.getNativeSource().release();
		    source.setNativeSource(null);
		}
	    }
	});
    }

    private boolean prepareSourceBuffer(AudioContext ctx, AudioSource comp) {
	if (comp.getNativeSource() != null) {
	    comp.getNativeSource().release();
	    comp.setNativeSource(null);
	}
	
	NativeAudioSource nat = ctx.getBackend().requestNativeSource();
	if (nat == null) {
	    return false;
	}
	comp.setNativeSource(nat);
	
	SampleSource ss = comp.getSource();

	// TODO: Cache audio data somewhere
	ShortBuffer data;
	try {
	    data = ss.readAllSamples();
	} catch (IOException e) {
	    throw new RuntimeException("Failed to read audio data", e);
	}

	AudioBuffer buf = comp.getBuffer() != null ? comp.getBuffer() : ctx.getBackend().createBuffer();
	buf.putData(ss.getFormat(), data, ss.getSampleRate());
	comp.setBuffer(buf);
	
	nat.attachBuffer(comp.getBuffer());
	nat.setGain(comp.getGain());
	nat.setPitch(comp.getPitch());

	return true;
    }

    private void onSourcePlay(AudioContext ctx, SourcePlayEvent event) {
	if (!prepareSourceBuffer(ctx, event.getSource()))
	    return;

	NativeAudioSource src = event.getSource().getNativeSource();
	src.setLooping(false);
	src.play();
    }

    private void onSourceLoop(AudioContext ctx, SourceLoopEvent event) {
	if (!prepareSourceBuffer(ctx, event.getSource()))
	    return;

	NativeAudioSource src = event.getSource().getNativeSource();
	src.setLooping(true);
	src.play();
    }

    private void onSourcePause(SourcePauseEvent event) {
	event.getSource().getNativeSource().pause();
    }

    private void onSourceResume(SourceResumeEvent event) {
	event.getSource().getNativeSource().play();
    }

    private void onSourceStop(SourceStopEvent event) {
	event.getSource().getNativeSource().stop();
    }

    @Override
    public void onEvent(World world, EventDispatcher d) {
	AudioContext ctx = world.getLocalSingleton(AudioContext.class);
	d.dispatch(SourcePlayEvent.class, (event) -> onSourcePlay(ctx, event));
	d.dispatch(SourceLoopEvent.class, (event) -> onSourceLoop(ctx, event));
	d.dispatch(SourcePauseEvent.class, this::onSourcePause);
	d.dispatch(SourceResumeEvent.class, this::onSourceResume);
	d.dispatch(SourceStopEvent.class, this::onSourceStop);
    }

    @Override
    public void close(World world) {
	world.forEach(AudioSource.class, (source) -> {
	    if (source.getNativeSource() != null) {
		source.getNativeSource().release();
	    }

	    if (source.getBuffer() != null) {
		source.getBuffer().delete();
	    }
	});
    }
}
