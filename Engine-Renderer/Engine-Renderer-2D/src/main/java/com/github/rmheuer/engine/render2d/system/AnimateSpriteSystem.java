package com.github.rmheuer.engine.render2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.render2d.component.SpriteAnimation;
import com.github.rmheuer.engine.render2d.component.SpriteRenderer;

public final class AnimateSpriteSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
        world.forEach(SpriteAnimation.class, SpriteRenderer.class, (anim, renderer) -> {
            if (renderer.getSprite() == null)
                renderer.setSprite(anim.getCurrentFrame());

            float time = anim.getTimeSinceLastFrame();
            time += delta;

            float interval = 1 / anim.getFramesPerSecond();
            while (time > interval) {
                int idx = anim.getCurrentFrameIndex();
                int count = anim.getFrames().size();

                idx++;
                if (idx >= count)
                    idx = 0;

                anim.setCurrentFrameIndex(idx);
                renderer.setSprite(anim.getCurrentFrame());

                time -= interval;
            }

            anim.setTimeSinceLastFrame(time);
        });
    }
}
