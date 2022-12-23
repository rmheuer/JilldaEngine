package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.AudioBackend;
import com.github.rmheuer.engine.audio.NativeAudioListener;
import com.github.rmheuer.engine.audio.component.AudioListener;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.PostSimulationGroup;
import com.github.rmheuer.engine.core.transform.PropagateTransformSystem;
import com.github.rmheuer.engine.core.transform.Transform;

public final class AudioUpdateListenerSystem implements GameSystem {
    @Override
    @RunInGroup(PostSimulationGroup.class)
    @After(PropagateTransformSystem.class)
    public void update(World world, float delta) {
        NativeAudioListener listener = AudioBackend.get().getListener();
        world.forEach(AudioListener.class, Transform.class, (l, tx) -> {
            listener.setPosition(tx.getGlobalPosition());
            listener.setOrientation(tx.getGlobalForward(), tx.getGlobalUp());
            listener.setGain(l.getGain());
        });
    }
}
