package com.github.rmheuer.engine.render2d.asset;

import com.github.rmheuer.engine.core.asset.AssetManager;
import com.github.rmheuer.engine.core.asset.AssetSerializer;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.serial.codec.bin.BinarySerialCodec;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render2d.font.TrueTypeFont;

import java.io.IOException;

public final class TrueTypeFontSerializer implements AssetSerializer<TrueTypeFont> {
    @Override
    public void serialize(TrueTypeFont asset, ResourceFile dst) throws IOException {
        SerialObject obj = new SerialObject();
        obj.putFloat("heightPx", asset.getHeightPx());
        obj.put("file", ObjectSerializer.get().serialize(asset.getTtfFile(), ResourceFile.class));
        BinarySerialCodec.get().encode(obj, dst.writeAsStream());
    }

    @Override
    public TrueTypeFont deserialize(ResourceFile src, AssetManager mgr) throws IOException {
        SerialObject obj = (SerialObject) BinarySerialCodec.get().decode(src.readAsStream());
        float heightPx = obj.getFloat("heightPx");
        ResourceFile ttf = ObjectSerializer.get().deserialize(obj.get("file"), ResourceFile.class);
        return new TrueTypeFont(ttf, heightPx);
    }
}
