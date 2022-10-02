package com.github.rmheuer.engine.physics2d;

import com.github.rmheuer.engine.core.ecs.component.WorldLocalSingleton;
import com.github.rmheuer.engine.core.math.Vector2f;
import org.jbox2d.dynamics.World;

public final class Physics2DContext implements WorldLocalSingleton {
    private final Vector2f currentGravity;
    private final World world;

    public Physics2DContext() {
        currentGravity = new Vector2f(0, 0);
        world = new World(Box2DUtils.toB2Vec(currentGravity));
    }

    public World getWorld() {
        return world;
    }

    public void setGravity(Vector2f grav) {
        if (grav.equals(currentGravity))
            return;

        currentGravity.set(grav);
        world.setGravity(Box2DUtils.toB2Vec(currentGravity));
    }
}
