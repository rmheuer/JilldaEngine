package com.github.rmheuer.engine.physics2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.physics2d.Physics2DContext;

public final class Physics2DContextSystem implements GameSystem {
    @Override
    public void init(World world) {
        Physics2DContext ctx = new Physics2DContext();
        world.setLocalSingleton(ctx);
    }
}
