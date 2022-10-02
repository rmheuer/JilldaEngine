package com.github.rmheuer.engine.physics2d;

import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.physics2d.component.RigidBody2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public final class Box2DUtils {
    // TODO: This should not be needed
    public static final float SCALE_FACTOR = 0.05f;

    public static Vec2 toB2Vec(Vector2f vec) {
        return new Vec2(vec.x*SCALE_FACTOR, vec.y*SCALE_FACTOR);
    }

    public static BodyType toB2Type(RigidBody2D.Type type) {
        switch (type) {
            case STATIC: return BodyType.STATIC;
            case DYNAMIC: return BodyType.DYNAMIC;
            case KINEMATIC: return BodyType.KINEMATIC;
            default:
                throw new IllegalArgumentException(String.valueOf(type));
        }
    }
}
