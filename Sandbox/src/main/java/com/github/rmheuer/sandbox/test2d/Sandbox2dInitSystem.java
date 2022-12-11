package com.github.rmheuer.sandbox.test2d;

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
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.core.transform.Transform;
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
import com.github.rmheuer.engine.render.camera.PerspectiveProjection;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render2d.component.Canvas2D;
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
//        canvas.addComponent(new KeyboardControl(200, 3.14f));

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

            spriteEnt = canvas.newChild("Sprite " + i);
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
            floorTx.setPosition(new Vector3f(300, 0, 1));
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
            floorTx.setPosition(new Vector3f(-300, 0, 1));
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

        Canvas2D guiCanvas2D = new Canvas2D(true);
        GuiCanvas gui = new GuiCanvas();
        StringBuilder textBox = new StringBuilder("Text box");
        gui.addWindow(new GuiWindow("Profiler", this::showProfileTool));
//
        Entity guiCanvasE = world.getRoot().newChild("Gui Canvas");
        Transform guiTx = new Transform();
//        guiTx.setPosition(new Vector3f(0, 0, 2f));
        guiCanvasE.addComponent(guiCanvas2D);
        guiCanvasE.addComponent(gui);
        guiCanvasE.addComponent(guiTx);
    }

    private void showProfileNode(GuiRenderer g, ProfileNode node) {
        List<ProfileNode> children = node.getChildren();

        int flags = children.isEmpty() ? GuiTreeFlags.Leaf : GuiTreeFlags.None;
        g.tableNextColumn();
        boolean open = g.pushTree(node.getName(), node.getName(), flags);
        g.tableNextColumn();
        g.text(String.format("%.3f", node.getSelfTime() / 1_000_000.0));
        g.tableNextColumn();
        g.text(String.format("%.3f", node.getTotalTime() / 1_000_000.0));

        if (open) {
            for (ProfileNode child : children) {
                showProfileNode(g, child);
            }
            g.popTree();
        }
    }

    private void showProfileTool(GuiRenderer g) {
        g.beginTableFlags(GuiTableFlags.NoPaddingY, 4, 1, 1);
        g.tableNextColumn();
        g.text("Name"); g.tableNextColumn();
        g.text("Self"); g.tableNextColumn();
        g.text("Total");

        Map<FixedProfileStage, ProfileNode> fixedData = Game.get().getStageProfileData();
        for (FixedProfileStage stage : FixedProfileStage.values()) {
            ProfileNode node = fixedData.get(stage);
            if (node != null)
                showProfileNode(g, node);
        }

        g.tableNextColumn();
        boolean open = g.pushTree("Global events");
        g.tableNextColumn();
        g.tableNextColumn();

        if (open) {
            // Alphabetize event entries
            List<Map.Entry<Class<? extends Event>, ProfileNode>> eventEntries = new ArrayList<>(Game.get().getEventProfileData().entrySet());
            eventEntries.sort((e1, e2) -> String.CASE_INSENSITIVE_ORDER.compare(e1.getKey().getSimpleName(), e2.getKey().getSimpleName()));

            for (Map.Entry<Class<? extends Event>, ProfileNode> entry : eventEntries) {
                showProfileNode(g, entry.getValue());
            }
            g.popTree();
        }

        g.endTable();
    }
}
