package com.github.rmheuer.sandbox.test2d;

import com.github.rmheuer.engine.audio.component.AudioListener;
import com.github.rmheuer.engine.audio.component.AudioSource;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.math.MathUtils;
import com.github.rmheuer.engine.core.math.Vector2f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.profile.FixedProfileStage;
import com.github.rmheuer.engine.core.profile.ProfileNode;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.debugtools.ProfileTool;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiTableFlags;
import com.github.rmheuer.engine.gui.GuiTreeFlags;
import com.github.rmheuer.engine.gui.GuiWindow;
import com.github.rmheuer.engine.gui.component.GuiCanvas;
import com.github.rmheuer.engine.physics2d.component.BoxCollider2D;
import com.github.rmheuer.engine.physics2d.component.Gravity2D;
import com.github.rmheuer.engine.physics2d.component.RigidBody2D;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.OrthographicProjection;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render.texture.Subimage;
import com.github.rmheuer.engine.render2d.component.Canvas2D;
import com.github.rmheuer.engine.render2d.component.SpriteAnimation;
import com.github.rmheuer.engine.render2d.component.SpriteRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Sandbox2dInitSystem implements GameSystem {
    @Override
    public void init(World world) {
        {
            Camera camera = new Camera(new OrthographicProjection(OrthographicProjection.ResizeRule.MAINTAIN_FIXED_HEIGHT));
            Transform cameraTx = new Transform();
            cameraTx.getPosition().z = 800;

            world.getRoot().newChild(
                    "Camera", camera, cameraTx,
                    new KeyboardControl(200, MathUtils.fPI),
                    new AudioListener()
            );
        }

        Subimage box = null, floor = null;
        try {
            box = Image.decode(new JarResourceFile("box.png"));
            floor = Image.decode(new JarResourceFile("floor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        {
            Gravity2D grav = new Gravity2D();
            grav.setAcceleration(new Vector2f(0, 500));
            world.getRoot().newChild("Gravity", grav);
        }

        Entity canvas = world.getRoot().newChild("Canvas", new Transform());

        int count = 25;
        int sqrtCount = (int) Math.sqrt(count);
        Entity spriteEnt = null;
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

            spriteEnt = canvas.newChild("Sprite " + i, sprite, spriteTx, spriteBd, spriteBox);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(0, 250, 0));
            floorTx.setScale(new Vector3f(700, 50, 1));
            RigidBody2D floorBd = new RigidBody2D();
            floorBd.setType(RigidBody2D.Type.STATIC);
            BoxCollider2D floorBox = new BoxCollider2D();

            canvas.newChild("Floor", floorSpr, floorTx, floorBd, floorBox);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(0, -250, 0));
            floorTx.setScale(new Vector3f(700, 50, 1));
            RigidBody2D floorBd = new RigidBody2D();
            floorBd.setType(RigidBody2D.Type.STATIC);
            BoxCollider2D floorBox = new BoxCollider2D();

            canvas.newChild("Ceiling", floorSpr, floorTx, floorBd, floorBox);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(300, 0, 1));
            floorTx.setRotation(new Vector3f(0, 0, (float) Math.PI / 2));
            floorTx.setScale(new Vector3f(700, 50, 1));
            RigidBody2D floorBd = new RigidBody2D();
            floorBd.setType(RigidBody2D.Type.STATIC);
            BoxCollider2D floorBox = new BoxCollider2D();

            canvas.newChild("Wall 1", floorSpr, floorTx, floorBd, floorBox);
        }

        {
            SpriteRenderer floorSpr = new SpriteRenderer(floor);
            Transform floorTx = new Transform();
            floorTx.setPosition(new Vector3f(-300, 0, 1));
            floorTx.setRotation(new Vector3f(0, 0, (float) Math.PI / 2));
            floorTx.setScale(new Vector3f(700, 50, 1));
            RigidBody2D floorBd = new RigidBody2D();
            floorBd.setType(RigidBody2D.Type.STATIC);
            BoxCollider2D floorBox = new BoxCollider2D();

            canvas.newChild("Wall 2", floorSpr, floorTx, floorBd, floorBox);
        }

        Canvas2D guiCanvas2D = new Canvas2D(true);
        GuiCanvas gui = new GuiCanvas();
        gui.addWindow(new GuiWindow("Profiler", new ProfileTool()));
        world.getRoot().newChild("Gui Canvas", new Transform(), guiCanvas2D, gui);

        ResourceFile file = new JarResourceFile("testsong.ogg");

        AudioSource s = new AudioSource(file);
        s.setLooping(true);
        s.setMode(AudioSource.Mode.SOURCE_2D);
        world.getRoot().newChild("Audio source", new Transform(), s);

        Image spriteSheet;
        try {
            spriteSheet = Image.decode(new JarResourceFile("thing.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SpriteRenderer animRenderer = new SpriteRenderer();
        SpriteAnimation anim = SpriteAnimation.fromSpriteSheet(10, spriteSheet, 0, 0, 16, 16, 6);
        Transform animTx = new Transform();
        animTx.setPosition(new Vector3f(0, 0, 0));
        animTx.setScale(new Vector3f(64, 64, 1));

        RigidBody2D animBd = new RigidBody2D();
        animBd.setType(RigidBody2D.Type.DYNAMIC);
        BoxCollider2D animBox = new BoxCollider2D();
        animBox.setRestitution(1.1f);

        world.getRoot().newChild("Animation test", animRenderer, anim, animTx, animBd, animBox);
    }
}
