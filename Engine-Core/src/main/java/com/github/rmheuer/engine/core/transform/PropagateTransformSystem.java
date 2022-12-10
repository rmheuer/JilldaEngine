package com.github.rmheuer.engine.core.transform;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.PostSimulationGroup;
import com.github.rmheuer.engine.core.math.Matrix4f;

public final class PropagateTransformSystem implements GameSystem {
    private void recursivePropagate(Entity e, Matrix4f parentMatrix) {
        Transform tx = e.getComponent(Transform.class);
        if (tx == null)
            return;

        parentMatrix.mul(tx.getMatrix(), tx.global);

        for (Entity child : e.getChildren()) {
            recursivePropagate(child, tx.getGlobalMatrix());
        }
    }

    @Override
    @RunInGroup(PostSimulationGroup.class)
    public void update(World world, float delta) {
        recursivePropagate(world.getRoot(), new Matrix4f());
    }
}
