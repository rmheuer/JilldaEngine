package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.audio.component.AudioSource;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;

public final class AudioSourceUpdatePropertiesSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
        world.forEach(AudioSource.class, (source) -> {
            AudioSource.Runtime runtime = source.getRuntime();
            if (runtime == null)
                return;
            NativeAudioSource nat = runtime.getSource();

            nat.setGain(source.getGain());
            nat.setPitch(source.getPitch());
            runtime.getLooper().setEnabled(source.isLooping());
        });
    }
}
