package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.AudioContext;
import com.github.rmheuer.engine.audio.Listener;
import com.github.rmheuer.engine.audio.component.AudioListener;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.math.Transform;

public final class AudioUpdateListenerSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
	AudioContext ctx = world.getLocalSingleton(AudioContext.class);
	Listener l = ctx.getBackend().getListener();
	
	world.forEach(AudioListener.class, Transform.class, (listener, tx) -> {
	    l.setPosition(tx.getPosition());
	    l.setOrientation(tx.getForward(), tx.getUp());
	});
    }
}
