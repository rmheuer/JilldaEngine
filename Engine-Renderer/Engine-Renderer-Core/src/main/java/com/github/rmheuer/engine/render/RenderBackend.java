package com.github.rmheuer.engine.render;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.mesh.Mesh;
import com.github.rmheuer.engine.render.mesh.PrimitiveType;
import com.github.rmheuer.engine.render.mesh.Vertex;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.Texture2D;
import com.github.rmheuer.engine.render.texture.TextureData;
import com.github.rmheuer.engine.render.texture.TextureSettings;

import java.io.IOException;

public interface RenderBackend {
    // Basic render commands
    void setViewportSize(int width, int height);
    void clear();
    
    // Primitive creators
    Window createWindow(WindowSettings settings);
    Shader createShader(ResourceFile res) throws IOException;
    ShaderProgram createShaderProgram(Shader... shaders);
    <T extends Vertex> Mesh<T> createMesh(PrimitiveType primType);
    Texture2D createTexture2D(ResourceFile res, TextureSettings settings) throws IOException;
    default Texture2D createTexture2D(ResourceFile res) throws IOException { return createTexture2D(res, new TextureSettings()); }
    Texture2D createTexture2D(TextureData data, TextureSettings settings);
    default Texture2D createTexture2D(TextureData data) { return createTexture2D(data, new TextureSettings()); }
    CubeMap createCubeMap(TextureSettings settings, TextureData posX, TextureData negX, TextureData posY, TextureData negY, TextureData posZ, TextureData negZ);
}
