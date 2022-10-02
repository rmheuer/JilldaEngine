package com.github.rmheuer.engine.physics2d.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.InitializationGroup;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.physics2d.Box2DUtils;
import com.github.rmheuer.engine.physics2d.Physics2DContext;
import com.github.rmheuer.engine.physics2d.component.BoxCollider2D;
import com.github.rmheuer.engine.physics2d.component.RigidBody2D;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import static com.github.rmheuer.engine.physics2d.Box2DUtils.SCALE_FACTOR;

public final class Physics2DCreateBodySystem implements GameSystem {
    // TODO: Delete runtime body when removed, and fixture when shape removed

    @Override
    @RunInGroup(InitializationGroup.class)
    public void fixedUpdate(World world) {
        Physics2DContext ctx = world.getLocalSingleton(Physics2DContext.class);
        world.forEachEntity(RigidBody2D.class, Transform.class, (entity, body, tx) -> {
            if (body.hasRuntimeBody())
                return;

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = Box2DUtils.toB2Type(body.getType());
            Vector3f pos = tx.getPosition();
            bodyDef.position.set(pos.x * SCALE_FACTOR, pos.y * SCALE_FACTOR);
            bodyDef.angle = tx.getRotation().z;

            Body b2Body = ctx.getWorld().createBody(bodyDef);
            b2Body.setFixedRotation(body.isFixedRotation());
            b2Body.setGravityScale(body.getGravityScale());

            body.setRuntimeBody(b2Body);

            if (entity.hasComponent(BoxCollider2D.class)) {
                BoxCollider2D collider = entity.getComponent(BoxCollider2D.class);

                Vector3f scale = tx.getScale();
                Vector2f size = collider.getSize();

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(scale.x * size.x / 2 * SCALE_FACTOR, scale.y * size.y / 2 * SCALE_FACTOR);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = collider.getDensity();
                fixtureDef.friction = collider.getFriction();
                fixtureDef.restitution = collider.getRestitution();
                b2Body.createFixture(fixtureDef);
            }
        });
    }
}
