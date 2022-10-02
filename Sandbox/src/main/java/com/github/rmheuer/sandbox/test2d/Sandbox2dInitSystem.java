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

        Image box = null, floor = null;
        try {
            box = Image.decode(new JarResourceFile("box.png"));
            floor = Image.decode(new JarResourceFile("floor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        {
//            Gravity grav = new Gravity();
//            grav.setAcceleration(new Vector3f(0, 200, 0));
//            world.getRoot().newChild("Gravity").addComponent(grav);
//        }

        {
            SpriteRenderer sprite = new SpriteRenderer(box);
            Transform spriteTx = new Transform();
            spriteTx.setScale(new Vector3f(100, 100, 1));
//            RigidBody spriteBd = new RigidBody();

            Entity spriteEnt = world.getRoot().newChild("Sprite");
            spriteEnt.addComponent(sprite);
            spriteEnt.addComponent(spriteTx);
//            spriteEnt.addComponent(spriteBd);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(0, 250, 0));
            floorTx.setScale(new Vector3f(700, 50, 1));
//            RigidBody floorBd = new RigidBody();
//            floorBd.setFixed(true);

            Entity floorEnt = world.getRoot().newChild("Floor");
            floorEnt.addComponent(floorSpr);
            floorEnt.addComponent(floorTx);
//            floorEnt.addComponent(floorBd);
        }
    }
}
