package com.github.rmheuer.engine.render2d.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.math.Vector4f;
import com.github.rmheuer.engine.render.texture.Colors;
import com.github.rmheuer.engine.render.texture.Subimage;

public final class SpriteRenderer implements Component {
    private Subimage sprite;
    private Vector4f tint;

    public SpriteRenderer() {
        this(null);
    }

    public SpriteRenderer(Subimage sprite) {
        this(sprite, new Vector4f(Colors.WHITE));
    }

    public SpriteRenderer(Subimage sprite, Vector4f tint) {
        this.sprite = sprite;
        this.tint = tint;
    }

    public Subimage getSprite() {
        return sprite;
    }

    public void setSprite(Subimage sprite) {
        this.sprite = sprite;
    }

    public Vector4f getTint() {
        return tint;
    }

    public void setTint(Vector4f tint) {
        this.tint = tint;
    }

    @Override
    public String toString() {
        return "SpriteRenderer{" +
                "sprite=" + sprite +
                ", tint=" + tint +
                '}';
    }
}
