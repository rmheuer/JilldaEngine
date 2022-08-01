package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.Image;

import java.io.IOException;
import java.util.List;

public final class Renderer2D {
    public static final int MAX_TEXTURE_SLOTS = 16;

    private static final String VERTEX_SHADER_PATH = "com/github/rmheuer/engine/render2d/shaders/vertex.glsl";
    private static final String FRAGMENT_SHADER_PATH = "com/github/rmheuer/engine/render2d/shaders/fragment.glsl";

    private final NativeObjectManager nom;
    private final Mesh<Vertex2D> mesh;
    private final ShaderProgram shader;
    private final Image whiteTex;

    public Renderer2D(NativeObjectManager nom) {
        this.nom = nom;
        mesh = new Mesh<>(PrimitiveType.TRIANGLES);
        try {
            shader = new ShaderProgram(
                    new Shader(ShaderType.VERTEX, new JarResourceFile(VERTEX_SHADER_PATH)),
                    new Shader(ShaderType.FRAGMENT, new JarResourceFile(FRAGMENT_SHADER_PATH))
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load built-in shaders", e);
        }

        int[] whiteData = {0xFFFFFFFF};
        whiteTex = new Image(1, 1, whiteData);

        for (int i = 0; i < MAX_TEXTURE_SLOTS; i++) {
            shader.getUniform("u_Textures[" + i + "]").setInt(i);
        }
    }

    public void setView(Camera camera, Transform cameraTx) {
        shader.getUniform("u_Projection").setMatrix4f(camera.getProjection().getMatrix());
        shader.getUniform("u_View").setMatrix4f(cameraTx.getGlobalInverseMatrix());
    }

    private void drawBatch(VertexBatch batch) {
        mesh.setData(batch.getData(), MeshDataUsage.DYNAMIC);

        ShaderProgram.Native nShader = shader.getNative(nom);

        nShader.bind();
        for (int i = 0; i < MAX_TEXTURE_SLOTS; i++) {
            Image tex = batch.getTextures()[i];
            if (tex != null) {
                tex.getNative(nom).bindToSlot(i);
            }
        }

        mesh.getNative(nom).render();
        nShader.unbind();
    }

    public void draw(DrawList2D list) {
        List<VertexBatch> batches = VertexBatcher2D.batch(list.getVertices(), list.getIndices(), whiteTex);
        for (VertexBatch batch : batches) {
            drawBatch(batch);
        }
    }
}
