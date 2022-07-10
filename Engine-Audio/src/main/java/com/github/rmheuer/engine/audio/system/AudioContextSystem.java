package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.AudioBackend;
import com.github.rmheuer.engine.audio.AudioContext;
import com.github.rmheuer.engine.audio.openal.OpenALBackend;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;

public final class AudioContextSystem implements GameSystem {
    @Override
    public void init(World world) {
	AudioBackend backend = new OpenALBackend();
	AudioContext ctx = new AudioContext(backend);
	world.setLocalSingleton(ctx);
    }

    @Override
    public void close(World world) {
	AudioContext ctx = world.getLocalSingleton(AudioContext.class);
	ctx.getBackend().deleteContext();
    }
}
