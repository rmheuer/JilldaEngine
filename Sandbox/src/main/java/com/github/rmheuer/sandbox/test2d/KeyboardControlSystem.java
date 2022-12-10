package com.github.rmheuer.sandbox.test2d;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.keyboard.Keyboard;
import com.github.rmheuer.engine.core.input.mouse.Mouse;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.math.Ray3f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.camera.PrimaryCamera;
import com.github.rmheuer.engine.render.system.RenderContext;

public final class KeyboardControlSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
        world.forEach(KeyboardControl.class, Transform.class, (control, tx) -> {
            float moveScale = delta * control.moveSpeed;
            Vector3f forward = tx.getForward().mul(moveScale);
            Vector3f up = tx.getUp().mul(moveScale);
            Vector3f right = tx.getRight().mul(moveScale);

            Keyboard kb = Game.get().getInputManager().getSource(Keyboard.class);
            Vector3f pos = tx.getPosition();
            if (kb.isKeyPressed(Key.W)) pos.add(forward);
            if (kb.isKeyPressed(Key.S)) pos.sub(forward);
            if (kb.isKeyPressed(Key.D)) pos.add(right);
            if (kb.isKeyPressed(Key.A)) pos.sub(right);
            if (kb.isKeyPressed(Key.SPACE)) pos.add(up);
            if (kb.isKeyPressed(Key.LEFT_SHIFT)) pos.sub(up);

            float turnScale = delta * control.turnSpeed;
            Vector3f rot = tx.getRotation();
            if (kb.isKeyPressed(Key.UP)) rot.x += turnScale;
            if (kb.isKeyPressed(Key.DOWN)) rot.x -= turnScale;
            if (kb.isKeyPressed(Key.LEFT)) rot.y += turnScale;
            if (kb.isKeyPressed(Key.RIGHT)) rot.y -= turnScale;
        });

//        Mouse mouse = Game.get().getInputManager().getSource(Mouse.class);
//        PrimaryCamera cam = world.getLocalSingleton(RenderContext.class).getPrimaryCamera();
//        if (cam != null) {
//            Ray3f ray = cam.getRayAtScreenPos(mouse.getCursorX(), mouse.getCursorY());
//            System.out.println(ray);
//        }
    }
}
