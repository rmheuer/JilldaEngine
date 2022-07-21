package com.github.rmheuer.sandbox;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.math.Transform;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.core.util.SizeOf;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.PerspectiveProjection;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshBuilder;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.mesh.Vertex;
import com.github.rmheuer.engine.render.mesh.VertexLayout;
import com.github.rmheuer.engine.render.shader.AttribType;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.system.RenderContextSystem;

import java.io.IOException;
import java.nio.ByteBuffer;

@After(stage = Stage.INIT, after = RenderContextSystem.class)
public final class SandboxInitSystem implements GameSystem {
    private static final class MyVertex implements Vertex {
        private static final int SIZEOF = 6 * SizeOf.FLOAT;
        private static final VertexLayout LAYOUT = new VertexLayout(
                AttribType.VEC3, // Position
                AttribType.VEC3  // Color
        );

        private final Vector3f pos;
        private final Vector3f col;

        public MyVertex(Vector3f pos, Vector3f col) {
            this.pos = pos;
            this.col = col;
        }

        @Override
        public int sizeOf() {
            return SIZEOF;
        }

        @Override
        public VertexLayout getLayout() {
            return LAYOUT;
        }

        @Override
        public void addToBuffer(ByteBuffer buf) {
            pos.put(buf);
            col.put(buf);
        }
    }

    private Mesh<MyVertex> mesh;
    private ShaderProgram shader;

    @Override
    public void init(World world) {
        Entity cam = world.getRoot().newChild();
        cam.addComponent(new Camera(new PerspectiveProjection()));
        Transform cameraTx = new Transform();
        cameraTx.getPosition().z = 5;
        cam.addComponent(cameraTx);

        RenderBackend b = RendererAPI.getBackend();

        mesh = b.createMesh(PrimitiveType.TRIANGLES);
        MeshBuilder<MyVertex> builder = new MeshBuilder<>();
        builder.vertex(new MyVertex(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector3f(1, 0, 0)));
        builder.vertex(new MyVertex(new Vector3f( 0.5f, -0.5f, 0.0f), new Vector3f(0, 1, 0)));
        builder.vertex(new MyVertex(new Vector3f( 0.0f,  0.5f, 0.0f), new Vector3f(0, 0, 1)));
        builder.indices(0, 1, 2);
        mesh.setData(builder, MeshDataUsage.STATIC);

        try {
            shader = b.createShaderProgram(
                    b.createShader(new JarResourceFile("vertex.glsl")),
                    b.createShader(new JarResourceFile("fragment.glsl"))
            );
            shader.claim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shaders");
        }
    }

    @Override
    public void onEvent(World world, EventDispatcher dispatch) {
        dispatch.dispatch(RenderSceneEvent.class, (event) -> {
            world.forEach(Camera.class, Transform.class, (cam, tx) -> {
                shader.bind();
                shader.getUniform("u_Projection").setMatrix4f(cam.getProjection().getMatrix());
                shader.getUniform("u_View").setMatrix4f(tx.getInverseMatrix());
                mesh.draw();
                shader.unbind();
            });
        });
    }

    @Override
    public void close(World world) {
        mesh.delete();
        shader.release();
    }
}
