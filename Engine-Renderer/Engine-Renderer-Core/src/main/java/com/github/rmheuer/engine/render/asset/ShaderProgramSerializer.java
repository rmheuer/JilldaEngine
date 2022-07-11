package com.github.rmheuer.engine.render.asset;

import com.github.rmheuer.engine.core.asset.AssetManager;
import com.github.rmheuer.engine.core.asset.AssetSerializer;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.serial.codec.bin.BinarySerialCodec;
import com.github.rmheuer.engine.core.serial.node.SerialArray;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render.shader.Shader;
import com.github.rmheuer.engine.render.shader.ShaderProgram;

import java.io.IOException;

public final class ShaderProgramSerializer implements AssetSerializer<ShaderProgram> {
    @Override
    public void serialize(ShaderProgram program, ResourceFile dst) throws IOException {
        SerialArray arr = new SerialArray();
        for (Shader shader : program.getShaders()) {
            arr.add(ObjectSerializer.get().serialize(shader.getSourceFile(), ResourceFile.class));
        }
        BinarySerialCodec.get().encode(arr, dst.writeAsStream());
    }

    @Override
    public ShaderProgram deserialize(ResourceFile src, AssetManager mgr) throws IOException {
        SerialArray arr = (SerialArray) BinarySerialCodec.get().decode(src.readAsStream());

        Shader[] shaders = new Shader[arr.size()];
        for (int i = 0; i < shaders.length; i++) {
            SerialNode node = arr.get(i);
            ResourceFile shaderRes = ObjectSerializer.get().deserialize(node, ResourceFile.class);
            shaders[i] = mgr.getAsset(Shader.class, shaderRes);
        }

        return RendererAPI.getBackend().createShaderProgram(shaders);
    }
}
