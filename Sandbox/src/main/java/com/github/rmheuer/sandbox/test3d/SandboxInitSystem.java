package com.github.rmheuer.sandbox.test3d;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.keyboard.Keyboard;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.core.util.Pair;
import com.github.rmheuer.engine.render.WindingOrder;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.PerspectiveProjection;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Image;
import com.github.rmheuer.engine.render3d.Primitives3D;
import com.github.rmheuer.engine.render3d.component.MeshRenderer;
import com.github.rmheuer.engine.render3d.loader.DefaultVertex;
import com.github.rmheuer.engine.render3d.loader.ObjLoader;
import com.github.rmheuer.engine.render3d.material.Material;

import java.io.IOException;
import java.util.List;

public final class SandboxInitSystem implements GameSystem {
    @Override
    public void init(World world) {
        Entity cam = world.getRoot().newChild();
        cam.addComponent(new Camera(new PerspectiveProjection()));
        Transform cameraTx = new Transform();
        cameraTx.getPosition().z = 5;
        cam.addComponent(cameraTx);
        cam.addComponent(new KeyboardControl());

        Pair<List<DefaultVertex>, List<Integer>> meshData;
        try {
            meshData = ObjLoader.loadObj(new JarResourceFile("snowman.obj"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load mesh", e);
        }
        Mesh<DefaultVertex> mesh = new Mesh<>(PrimitiveType.TRIANGLES);
        mesh.setData(meshData.getA(), meshData.getB(), MeshDataUsage.STATIC);

        ShaderProgram shader;
        Image texture;
        try {
            shader = new ShaderProgram(
                    new Shader(ShaderType.VERTEX, new JarResourceFile("vertex.glsl")),
                    new Shader(ShaderType.FRAGMENT, new JarResourceFile("fragment.glsl"))
            );
            texture = Image.decode(new JarResourceFile("snowman-tex.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load snowman assets", e);
        }
        Material mat = new Material(shader);
        mat.setImage("m_Texture", texture);

        MeshRenderer r = new MeshRenderer();
        r.setMesh(mesh);
        r.setMaterial(mat);

        Entity entity = world.getRoot();
        for (int i = 0; i < 10; i++) {
            entity = entity.newChild();

            Transform tx = new Transform();
            tx.getPosition().y = 5;

            entity.addComponent(r);
            entity.addComponent(tx);
            Spin spin = new Spin();
            spin.speeds.set((float) Math.random(), (float) Math.random(), (float) Math.random());
            entity.addComponent(spin);
        }

        ShaderProgram skyboxShader;
        CubeMap skyboxTex;
        try {
            skyboxShader = new ShaderProgram(
                    new Shader(ShaderType.VERTEX, new JarResourceFile("skybox/vertex.glsl")),
                    new Shader(ShaderType.FRAGMENT, new JarResourceFile("skybox/fragment.glsl"))
            );
            skyboxTex = new CubeMap();
            skyboxTex.setPosX(Image.decode(new JarResourceFile("skybox/skybox_left.png")));
            skyboxTex.setPosY(Image.decode(new JarResourceFile("skybox/skybox_up.png")));
            skyboxTex.setPosZ(Image.decode(new JarResourceFile("skybox/skybox_front.png")));
            skyboxTex.setNegX(Image.decode(new JarResourceFile("skybox/skybox_right.png")));
            skyboxTex.setNegY(Image.decode(new JarResourceFile("skybox/skybox_down.png")));
            skyboxTex.setNegZ(Image.decode(new JarResourceFile("skybox/skybox_back.png")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load skybox assets", e);
        }
        Material skyMat = new Material(skyboxShader);
        skyMat.setCubeMap("m_Texture", skyboxTex);

        MeshRenderer skyRenderer = new MeshRenderer();
        skyRenderer.setMesh(Primitives3D.CUBE);
        skyRenderer.setWindingOrder(WindingOrder.CLOCKWISE);
        skyRenderer.setMaterial(skyMat);

        Entity skybox = world.getRoot().newChild();
        skybox.addComponent(skyRenderer);
        Transform skyboxTx = new Transform();
        skyboxTx.getScale().mul(500);
        skybox.addComponent(skyboxTx);
        skybox.addComponent(new AlignPosition(cameraTx));
    }

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

        world.forEach(AlignPosition.class, Transform.class, (align, tx) -> {
            tx.setPosition(new Vector3f(align.target.getPosition()));
        });

        world.forEach(Spin.class, Transform.class, (spin, tx) -> {
            tx.getRotation().add(new Vector3f(spin.speeds).mul(delta));
        });
    }
}
