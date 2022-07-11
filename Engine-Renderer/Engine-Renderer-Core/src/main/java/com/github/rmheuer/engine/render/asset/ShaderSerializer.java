package com.github.rmheuer.engine.render.asset;

import com.github.rmheuer.engine.core.asset.AssetManager;
import com.github.rmheuer.engine.core.asset.AssetSerializer;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.shader.Shader;

import java.io.IOException;

public final class ShaderSerializer implements AssetSerializer<Shader> {
    @Override
    public void serialize(Shader asset, ResourceFile dst) throws IOException {
        throw new IOException("Can't serialize a shader");
    }

    @Override
    public Shader deserialize(ResourceFile src, AssetManager mgr) throws IOException {
        return RendererAPI.getBackend().createShader(src);
    }
}
