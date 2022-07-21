package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.texture.Texture2D;
import com.github.rmheuer.engine.render.texture.TextureData;

import java.io.IOException;
import java.util.List;

public final class Renderer2D {
    public static final int MAX_TEXTURE_SLOTS = 16;

    private static final String VERTEX_SHADER_PATH = "com/github/rmheuer/engine/render2d/shaders/vertex.glsl";
    private static final String FRAGMENT_SHADER_PATH = "com/github/rmheuer/engine/render2d/shaders/fragment.glsl";

    private final Mesh<Vertex2D> mesh;
    private final ShaderProgram shader;
    private final Texture2D whiteTex;

    public Renderer2D() {
        RenderBackend backend = RendererAPI.getBackend();
        mesh = backend.createMesh(PrimitiveType.TRIANGLES);
        mesh.claim();
        try {
            shader = backend.createShaderProgram(
                    backend.createShader(new JarResourceFile(VERTEX_SHADER_PATH)),
                    backend.createShader(new JarResourceFile(FRAGMENT_SHADER_PATH))
            );
            shader.claim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load built-in shaders", e);
        }

        byte[] whiteData = {
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
        };
        whiteTex = backend.createTexture2D(TextureData.fromByteArray(whiteData, 1, 1));
        whiteTex.claim();

        shader.bind();
        for (int i = 0; i < MAX_TEXTURE_SLOTS; i++) {
            shader.getUniform("u_Textures[" + i + "]").setInt(i);
        }
        shader.unbind();
    }

    public void setView(Camera camera, Transform cameraTx) {
        shader.bind();
        shader.getUniform("u_Projection").setMatrix4f(camera.getProjection().getMatrix());
        shader.getUniform("u_View").setMatrix4f(cameraTx.getGlobalInverseMatrix());
        shader.unbind();
    }

    private void drawBatch(VertexBatch batch) {
        mesh.setData(batch.getData(), MeshDataUsage.DYNAMIC);

        shader.bind();
        for (int i = 0; i < MAX_TEXTURE_SLOTS; i++) {
            Texture2D tex = batch.getTextures()[i];
            if (tex != null) {
                tex.bindToSlot(i);
            }
        }

        mesh.draw();
        shader.unbind();
    }

    public void draw(DrawList2D list) {
        List<VertexBatch> batches = VertexBatcher2D.batch(list.getVertices(), list.getIndices(), whiteTex);
        for (VertexBatch batch : batches) {
            drawBatch(batch);
        }
    }

    public void delete() {
        whiteTex.release();
        shader.release();
        mesh.release();
    }
}
