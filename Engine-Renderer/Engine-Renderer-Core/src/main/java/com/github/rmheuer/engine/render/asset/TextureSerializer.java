package com.github.rmheuer.engine.render.asset;

import com.github.rmheuer.engine.core.asset.AssetManager;
import com.github.rmheuer.engine.core.asset.AssetSerializer;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.serial.codec.bin.BinarySerialCodec;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.texture.Texture2D;
import com.github.rmheuer.engine.render.texture.TextureData;
import com.github.rmheuer.engine.render.texture.TextureSettings;

import java.io.IOException;

public final class TextureSerializer implements AssetSerializer<Texture2D> {
    @Override
    public void serialize(Texture2D asset, ResourceFile dst) throws IOException {
        SerialObject obj = new SerialObject();
        obj.put("settings", ObjectSerializer.get().serialize(asset.getSettings()));
        obj.put("data", ObjectSerializer.get().serialize(asset.getSourceData().getSourceFile(), ResourceFile.class));
        BinarySerialCodec.get().encode(obj, dst.writeAsStream());
    }

    @Override
    public Texture2D deserialize(ResourceFile src, AssetManager mgr) throws IOException {
        SerialObject obj = (SerialObject) BinarySerialCodec.get().decode(src.readAsStream());
        TextureSettings settings = ObjectSerializer.get().deserialize(obj.get("settings"), TextureSettings.class);
        TextureData data = mgr.getAsset(TextureData.class, ObjectSerializer.get().deserialize(obj.get("data"), ResourceFile.class));
        return RendererAPI.getBackend().createTexture(data, settings);
    }
}
