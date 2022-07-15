package com.github.rmheuer.engine.render.asset;

import com.github.rmheuer.engine.core.asset.AssetManager;
import com.github.rmheuer.engine.core.asset.AssetSerializer;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.serial.codec.bin.BinarySerialCodec;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.texture.CubeMap;
import com.github.rmheuer.engine.render.texture.CubeMapBuilder;
import com.github.rmheuer.engine.render.texture.TextureData;
import com.github.rmheuer.engine.render.texture.TextureSettings;

import java.io.IOException;

public final class CubeMapSerializer implements AssetSerializer<CubeMap> {
    private SerialNode serializeData(TextureData data) {
        return ObjectSerializer.get().serialize(data.getSourceFile(), ResourceFile.class);
    }

    @Override
    public void serialize(CubeMap asset, ResourceFile dst) throws IOException {
        SerialObject obj = new SerialObject();
        obj.put("settings", ObjectSerializer.get().serialize(asset.getSettings()));
        obj.put("+x", serializeData(asset.getPositiveXData()));
        obj.put("-x", serializeData(asset.getNegativeXData()));
        obj.put("+y", serializeData(asset.getPositiveYData()));
        obj.put("-y", serializeData(asset.getNegativeYData()));
        obj.put("+z", serializeData(asset.getPositiveZData()));
        obj.put("-z", serializeData(asset.getNegativeZData()));
        BinarySerialCodec.get().encode(obj, dst.writeAsStream());
    }

    private TextureData deserializeData(AssetManager mgr, SerialNode node) {
        return mgr.getAsset(TextureData.class, ObjectSerializer.get().deserialize(node, ResourceFile.class));
    }

    @Override
    public CubeMap deserialize(ResourceFile src, AssetManager mgr) throws IOException {
        SerialObject obj = (SerialObject) BinarySerialCodec.get().decode(src.readAsStream());
        TextureSettings settings = ObjectSerializer.get().deserialize(obj.get("settings"), TextureSettings.class);

        return new CubeMapBuilder(settings)
                .setPositiveXFace(deserializeData(mgr, obj.get("+x")))
                .setNegativeXFace(deserializeData(mgr, obj.get("-x")))
                .setPositiveYFace(deserializeData(mgr, obj.get("+y")))
                .setNegativeYFace(deserializeData(mgr, obj.get("-y")))
                .setPositiveZFace(deserializeData(mgr, obj.get("+z")))
                .setNegativeZFace(deserializeData(mgr, obj.get("-z")))
                .build();
    }
}
