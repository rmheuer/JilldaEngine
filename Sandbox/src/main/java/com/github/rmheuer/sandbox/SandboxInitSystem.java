package com.github.rmheuer.sandbox;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;
import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.keyboard.Keyboard;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.core.util.Pair;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.PerspectiveProjection;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.system.RenderContextSystem;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.CubeMapBuilder;
import com.github.rmheuer.engine.render.texture.Texture2D;
import com.github.rmheuer.engine.render.texture.TextureSettings;
import com.github.rmheuer.engine.render3d.Primitives3D;
import com.github.rmheuer.engine.render3d.component.MeshRenderer;
import com.github.rmheuer.engine.render3d.loader.DefaultVertex;
import com.github.rmheuer.engine.render3d.loader.ObjLoader;
import com.github.rmheuer.engine.render3d.material.Material;

import java.io.IOException;
import java.util.List;

@After(stage = Stage.INIT, after = RenderContextSystem.class)
public final class SandboxInitSystem implements GameSystem {
    @Override
    public void init(World world) {
        Entity cam = world.getRoot().newChild();
        cam.addComponent(new Camera(new PerspectiveProjection()));
        Transform cameraTx = new Transform();
        cameraTx.getPosition().z = 5;
        cam.addComponent(cameraTx);
        cam.addComponent(new KeyboardControl());

        RenderBackend b = RendererAPI.getBackend();

        Pair<List<DefaultVertex>, List<Integer>> meshData;
        try {
            meshData = ObjLoader.loadObj(new JarResourceFile("snowman.obj"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load mesh", e);
        }
        Mesh<DefaultVertex> mesh = b.createMesh(PrimitiveType.TRIANGLES);
        mesh.setData(meshData.getA(), meshData.getB(), MeshDataUsage.STATIC);

        ShaderProgram shader;
        try {
            shader = b.createShaderProgram(
                    b.createShader(new JarResourceFile("vertex.glsl")),
                    b.createShader(new JarResourceFile("fragment.glsl"))
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shaders", e);
        }

        Texture2D texture;
        try {
            texture = b.createTexture2D(new JarResourceFile("snowman-tex.png"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture", e);
        }

        Material mat = new Material(shader);
        mat.setTexture2D("m_Texture", texture);

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
            skyboxShader = b.createShaderProgram(
                    b.createShader(new JarResourceFile("skybox/vertex.glsl")),
                    b.createShader(new JarResourceFile("skybox/fragment.glsl"))
            );
            skyboxTex = new CubeMapBuilder(new TextureSettings())
                    .setPositiveXFace(new JarResourceFile("skybox/skybox_left.png"))
                    .setNegativeXFace(new JarResourceFile("skybox/skybox_right.png"))
                    .setPositiveYFace(new JarResourceFile("skybox/skybox_up.png"))
                    .setNegativeYFace(new JarResourceFile("skybox/skybox_down.png"))
                    .setPositiveZFace(new JarResourceFile("skybox/skybox_front.png"))
                    .setNegativeZFace(new JarResourceFile("skybox/skybox_back.png"))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load skybox", e);
        }
        Material skyMat = new Material(skyboxShader);
        skyMat.setCubeMap("m_Texture", skyboxTex);

        MeshRenderer skyRenderer = new MeshRenderer();
        skyRenderer.setMesh(Primitives3D.CUBE);
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
