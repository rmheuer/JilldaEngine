package com.github.rmheuer.engine.core.asset;

import com.github.rmheuer.engine.core.nat.RefCounted;
import com.github.rmheuer.engine.core.resource.ResourceFile;

public abstract class Asset extends RefCounted {
    private ResourceFile assetKey;

    public ResourceFile getAssetKey() {
        return assetKey;
    }

    public void setAssetKey(ResourceFile assetKey) {
        this.assetKey = assetKey;
    }
}
