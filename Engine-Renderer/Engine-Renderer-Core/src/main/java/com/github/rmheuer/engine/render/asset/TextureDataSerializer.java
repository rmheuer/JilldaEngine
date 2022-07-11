package com.github.rmheuer.engine.render.asset;

import com.github.rmheuer.engine.core.asset.AssetManager;
import com.github.rmheuer.engine.core.asset.AssetSerializer;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.render.texture.TextureData;

import java.io.IOException;

// Will load texture data from an image file
public final class TextureDataSerializer implements AssetSerializer<TextureData> {
    @Override
    public void serialize(TextureData asset, ResourceFile dst) throws IOException {
        throw new IOException("Not supported");
    }

    @Override
    public TextureData deserialize(ResourceFile src, AssetManager mgr) throws IOException {
        return TextureData.decode(src);
    }
}
