package com.github.rmheuer.engine.core.asset;

import com.github.rmheuer.engine.core.resource.ResourceFile;

import java.io.IOException;

public interface AssetSerializer<T extends Asset> {
    void serialize(T asset, ResourceFile dst) throws IOException;

    T deserialize(ResourceFile src, AssetManager mgr) throws IOException;
}
