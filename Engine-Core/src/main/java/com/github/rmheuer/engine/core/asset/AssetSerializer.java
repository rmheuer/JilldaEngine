package com.github.rmheuer.engine.core.asset;

import com.github.rmheuer.engine.core.resource.ResourceFile;

public interface AssetSerializer<T extends Asset> {
    default T load(ResourceFile file) {
        throw new UnsupportedOperationException("Loading is not supported");
    }

    default void save(T t, ResourceFile file) {
        throw new UnsupportedOperationException("Saving is not supported");
    }
}
