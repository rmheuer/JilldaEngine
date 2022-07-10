package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.math.Transform;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.render.RenderBackend;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.MeshDataUsage;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.shader.ShaderType;
import com.github.rmheuer.engine.render.texture.Texture;
import com.github.rmheuer.engine.render.texture.TextureData;

import java.io.IOException;
import java.util.List;

public final class Renderer2D {
    public static final int MAX_TEXTURE_SLOTS = 16;

    private static final String VERTEX_SHADER_PATH = "com/github/rmheuer/engine/render2d/shaders/vertex.glsl";
    private static final String FRAGMENT_SHADER_PATH = "com/github/rmheuer/engine/render2d/shaders/fragment.glsl";

    private final RenderBackend backend;
    private final Mesh<Vertex2D> mesh;
    private final ShaderProgram shader;
    private final Texture whiteTex;

    public Renderer2D(RenderBackend backend) {
        this.backend = backend;
        mesh = backend.createMesh(PrimitiveType.TRIANGLES);
        try {
            shader = backend.createShaderProgram(
                    backend.createShader(new JarResourceFile(VERTEX_SHADER_PATH), ShaderType.VERTEX),
                    backend.createShader(new JarResourceFile(FRAGMENT_SHADER_PATH), ShaderType.FRAGMENT)
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load built-in shaders");
        }

        byte[] whiteData = {
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
        };
        whiteTex = backend.createTexture(TextureData.fromByteArray(whiteData, 1, 1));

        shader.bind();
        for (int i = 0; i < MAX_TEXTURE_SLOTS; i++) {
            shader.getUniform("u_Textures[" + i + "]").setInt(i);
        }
        shader.unbind();
    }

    public RenderBackend getBackend() {
        return backend;
    }

    public void setView(Camera camera, Transform cameraTx) {
        shader.bind();
        shader.getUniform("u_Projection").setMatrix4f(camera.getProjection().getMatrix());
        shader.getUniform("u_View").setMatrix4f(cameraTx.getInverseMatrix());
        shader.unbind();
    }

    private void drawBatch(VertexBatch batch) {
        mesh.setData(batch.getData(), MeshDataUsage.DYNAMIC);

        shader.bind();
        for (int i = 0; i < MAX_TEXTURE_SLOTS; i++) {
            Texture tex = batch.getTextures()[i];
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
        whiteTex.delete();
        shader.delete();
        mesh.delete();
    }
}
