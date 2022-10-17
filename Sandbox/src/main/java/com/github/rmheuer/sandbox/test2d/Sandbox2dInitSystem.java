package com.github.rmheuer.sandbox.test2d;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.gui.component.GuiWindow;
import com.github.rmheuer.engine.physics2d.component.BoxCollider2D;
import com.github.rmheuer.engine.physics2d.component.Gravity2D;
import com.github.rmheuer.engine.physics2d.component.RigidBody2D;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.OrthographicProjection;
import com.github.rmheuer.engine.render.camera.PerspectiveProjection;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render2d.component.SpriteRenderer;

import java.io.IOException;

public final class Sandbox2dInitSystem implements GameSystem {
    @Override
    public void init(World world) {
        {
            Camera camera = new Camera(new PerspectiveProjection());
            Transform cameraTx = new Transform();
            cameraTx.getPosition().z = 800;

            Entity cameraEnt = world.getRoot().newChild("Camera");
            cameraEnt.addComponent(camera);
            cameraEnt.addComponent(cameraTx);
            cameraEnt.addComponent(new KeyboardControl(200, MathUtils.fPI));
        }

        Image box = null, floor = null;
        try {
            box = Image.decode(new JarResourceFile("box.png"));
            floor = Image.decode(new JarResourceFile("floor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        {
            Gravity2D grav = new Gravity2D();
            grav.setAcceleration(new Vector2f(0, 500));
            world.getRoot().newChild("Gravity").addComponent(grav);
        }

        Entity canvas = world.getRoot().newChild("Canvas");
        canvas.addComponent(new Transform());
        canvas.addComponent(new KeyboardControl(200, 3.14f));

        int count = 25;
        int sqrtCount = (int) Math.sqrt(count);
        for (int i = 0; i < count; i++) {
            SpriteRenderer sprite = new SpriteRenderer(box);
            Transform spriteTx = new Transform();
            spriteTx.setPosition(new Vector3f(MathUtils.map(i % sqrtCount, 0, sqrtCount - 1, -225, 225), MathUtils.map(i / sqrtCount, 0, count / sqrtCount - 1, -200, 200), 0));
            spriteTx.setScale(new Vector3f(50, 50, 1));
            spriteTx.setRotation(new Vector3f(0, 0, 0.75f + i * 0.2f));
            RigidBody2D spriteBd = new RigidBody2D();
            spriteBd.setType(RigidBody2D.Type.DYNAMIC);
            BoxCollider2D spriteBox = new BoxCollider2D();
            spriteBox.setRestitution(1.1f);

            Entity spriteEnt = canvas.newChild("Sprite " + i);
            spriteEnt.addComponent(sprite);
            spriteEnt.addComponent(spriteTx);
            spriteEnt.addComponent(spriteBd);
            spriteEnt.addComponent(spriteBox);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(0, 250, 0));
            floorTx.setScale(new Vector3f(700, 50, 1));
            RigidBody2D floorBd = new RigidBody2D();
            floorBd.setType(RigidBody2D.Type.STATIC);
            BoxCollider2D floorBox = new BoxCollider2D();

            Entity floorEnt = canvas.newChild("Floor");
            floorEnt.addComponent(floorSpr);
            floorEnt.addComponent(floorTx);
            floorEnt.addComponent(floorBd);
            floorEnt.addComponent(floorBox);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(0, -250, 0));
            floorTx.setScale(new Vector3f(700, 50, 1));
            RigidBody2D floorBd = new RigidBody2D();
            floorBd.setType(RigidBody2D.Type.STATIC);
            BoxCollider2D floorBox = new BoxCollider2D();

            Entity floorEnt = canvas.newChild("Ceiling");
            floorEnt.addComponent(floorSpr);
            floorEnt.addComponent(floorTx);
            floorEnt.addComponent(floorBd);
            floorEnt.addComponent(floorBox);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(300, 0, 0));
            floorTx.setRotation(new Vector3f(0, 0, (float) Math.PI / 2));
            floorTx.setScale(new Vector3f(700, 50, 1));
            RigidBody2D floorBd = new RigidBody2D();
            floorBd.setType(RigidBody2D.Type.STATIC);
            BoxCollider2D floorBox = new BoxCollider2D();

            Entity floorEnt = canvas.newChild("Wall 1");
            floorEnt.addComponent(floorSpr);
            floorEnt.addComponent(floorTx);
            floorEnt.addComponent(floorBd);
            floorEnt.addComponent(floorBox);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(-300, 0, 0));
            floorTx.setRotation(new Vector3f(0, 0, (float) Math.PI / 2));
            floorTx.setScale(new Vector3f(700, 50, 1));
            RigidBody2D floorBd = new RigidBody2D();
            floorBd.setType(RigidBody2D.Type.STATIC);
            BoxCollider2D floorBox = new BoxCollider2D();

            Entity floorEnt = canvas.newChild("Wall 2");
            floorEnt.addComponent(floorSpr);
            floorEnt.addComponent(floorTx);
            floorEnt.addComponent(floorBd);
            floorEnt.addComponent(floorBox);
        }

        world.getRoot().newChild("Gui").addComponent(new GuiWindow(
                (g) -> {
                   g.text("Hello");
                }
        ));
    }
}
