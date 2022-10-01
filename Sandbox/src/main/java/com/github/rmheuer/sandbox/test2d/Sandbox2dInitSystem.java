package com.github.rmheuer.sandbox.test2d;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.OrthographicProjection;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render2d.component.SpriteRenderer;

import java.io.IOException;

public final class Sandbox2dInitSystem implements GameSystem {
    @Override
    public void init(World world) {
        {
            Camera camera = new Camera(new OrthographicProjection());
            Transform cameraTx = new Transform();

            Entity cameraEnt = world.getRoot().newChild("Camera");
            cameraEnt.addComponent(camera);
            cameraEnt.addComponent(cameraTx);
        }

        {
            Image img = null;
            try {
                img = Image.decode(new JarResourceFile("snowman-tex.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            SpriteRenderer sprite = new SpriteRenderer(img);
            Transform spriteTx = new Transform();
            spriteTx.setScale(new Vector3f(100, 100, 1));
            spriteTx.setRotation(new Vector3f(0, 0, 1.0f));

            Entity spriteEnt = world.getRoot().newChild("Sprite");
            spriteEnt.addComponent(sprite);
            spriteEnt.addComponent(spriteTx);
        }
    }
}
