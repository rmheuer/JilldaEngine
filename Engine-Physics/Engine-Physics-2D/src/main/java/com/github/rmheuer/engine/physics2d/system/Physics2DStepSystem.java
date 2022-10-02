package com.github.rmheuer.engine.physics2d.system;

import com.github.rmheuer.engine.core.Time;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.PostSimulationGroup;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.physics2d.Physics2DContext;
import com.github.rmheuer.engine.physics2d.component.Gravity2D;
import com.github.rmheuer.engine.physics2d.component.RigidBody2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import static com.github.rmheuer.engine.physics2d.Box2DUtils.SCALE_FACTOR;

public final class Physics2DStepSystem implements GameSystem {
    @Override
    @RunInGroup(PostSimulationGroup.class)
    public void fixedUpdate(World world) {
        Physics2DContext ctx = world.getLocalSingleton(Physics2DContext.class);

        Vector2f gravity = new Vector2f(0, 0);
        world.forEach(Gravity2D.class, (grav) -> {
            gravity.add(grav.getAcceleration());
        });
        ctx.setGravity(gravity);

        ctx.getWorld().step(Time.getFixedDelta(), 6, 2);

        world.forEach(RigidBody2D.class, Transform.class, (body, tx) -> {
            if (!body.hasRuntimeBody())
                return;

            Body b2Body = (Body) body.getRuntimeBody();
            Vec2 b2Pos = b2Body.getPosition();

            Vector3f pos = tx.getPosition();
            pos.x = b2Pos.x / SCALE_FACTOR;
            pos.y = b2Pos.y / SCALE_FACTOR;
            tx.getRotation().z = b2Body.getAngle();
        });
    }
}
