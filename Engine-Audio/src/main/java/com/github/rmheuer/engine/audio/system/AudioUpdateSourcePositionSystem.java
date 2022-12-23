package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.audio.component.AudioSource;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.PostSimulationGroup;
import com.github.rmheuer.engine.core.transform.Transform;

public final class AudioUpdateSourcePositionSystem implements GameSystem {
    @Override
    @RunInGroup(PostSimulationGroup.class)
    public void update(World world, float delta) {
        world.forEach(AudioSource.class, Transform.class, (source, tx) -> {
            AudioSource.Runtime runtime = source.getRuntime();
            if (runtime == null)
                return;

            NativeAudioSource nat = runtime.getSource();
            nat.setPosition(tx.getGlobalPosition());
        });
    }
}
