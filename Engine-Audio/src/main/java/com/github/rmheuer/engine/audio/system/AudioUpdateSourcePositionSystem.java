package com.github.rmheuer.engine.audio.system;

import com.github.rmheuer.engine.audio.NativeAudioSource;
import com.github.rmheuer.engine.audio.component.AudioSource;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.PostSimulationGroup;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.transform.PropagateTransformSystem;
import com.github.rmheuer.engine.core.transform.Transform;

public final class AudioUpdateSourcePositionSystem implements GameSystem {
    @Override
    @RunInGroup(PostSimulationGroup.class)
    @After(PropagateTransformSystem.class)
    public void update(World world, float delta) {
        world.forEach(AudioSource.class, Transform.class, (source, tx) -> {
            AudioSource.Runtime runtime = source.getRuntime();
            if (runtime == null)
                return;

            NativeAudioSource nat = runtime.getSource();
            if (source.getMode() == AudioSource.Mode.SOURCE_3D)
                // Put the source where the object is
                nat.setGlobalPosition(tx.getGlobalPosition());
            else
                // Put the source directly at the listener
                nat.setListenerRelativePosition(new Vector3f());
        });
    }
}
