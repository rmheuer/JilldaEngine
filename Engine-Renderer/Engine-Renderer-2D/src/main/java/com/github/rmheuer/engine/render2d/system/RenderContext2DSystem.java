package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.Before;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.render.system.RenderContextSystem;
import com.github.rmheuer.engine.render2d.RenderContext2D;
import com.github.rmheuer.engine.render2d.Renderer2D;
import com.github.rmheuer.engine.render2d.asset.TrueTypeFontSerializer;
import com.github.rmheuer.engine.render2d.font.TrueTypeFont;

@After(stage = Stage.INIT, after = RenderContextSystem.class)
@Before(stage = Stage.CLOSE, before = RenderContextSystem.class)
public final class RenderContext2DSystem implements GameSystem {
    @Override
    public void init(World world) {
        RenderContext2D ctx2d = new RenderContext2D();
        world.setLocalSingleton(ctx2d);

        Game.get().getAssetManager().registerSerializer(TrueTypeFont.class, new TrueTypeFontSerializer());

        ctx2d.setRenderer(new Renderer2D());
    }

    @Override
    public void close(World world) {
        RenderContext2D ctx = world.getLocalSingleton(RenderContext2D.class);
        ctx.getRenderer().delete();
    }
}
