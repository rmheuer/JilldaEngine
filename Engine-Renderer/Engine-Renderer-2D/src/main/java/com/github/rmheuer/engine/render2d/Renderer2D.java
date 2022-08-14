package com.github.rmheuer.engine.render2d;

import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.render.RenderConstants;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.Projection;
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

        for (int i = 0; i < RenderConstants.MAX_TEXTURE_SLOTS; i++) {
            shader.getUniform("u_Textures[" + i + "]").setInt(i);
        }
    }

    public void setView(Projection proj, Transform cameraTx) {
        shader.getUniform("u_Projection").setMatrix4f(proj.getMatrix());
        shader.getUniform("u_View").setMatrix4f(cameraTx.getGlobalInverseMatrix());
    }

    private void drawBatch(VertexBatch batch) {
        mesh.setData(batch.getData(), MeshDataUsage.DYNAMIC);

        // Natives must be pre-obtained to prevent accidental state changes
        ShaderProgram.Native nShader = shader.getNative(nom);
        Mesh.Native nMesh = mesh.getNative(nom);
        Image.Native[] nTextures = new Image.Native[RenderConstants.MAX_TEXTURE_SLOTS];

        for (int i = 0; i < nTextures.length; i++) {
            Image tex = batch.getTextures()[i];
            if (tex != null) {
                nTextures[i] = tex.getNative(nom);
            }
        }

        nShader.bind();
        for (int i = 0; i < nTextures.length; i++) {
            Image.Native nTex = nTextures[i];
            if (nTex != null) {
                nTex.bindToSlot(i);
            }
        }

        nShader.updateUniformValues();
        nMesh.render();
        nShader.unbind();
    }

    public void draw(DrawList2D list) {
        List<VertexBatch> batches = VertexBatcher2D.batch(list.getVertices(), list.getIndices(), whiteTex);
        for (VertexBatch batch : batches) {
            drawBatch(batch);
        }
    }
}
