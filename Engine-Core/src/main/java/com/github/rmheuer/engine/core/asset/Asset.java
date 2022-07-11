package com.github.rmheuer.engine.core.asset;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.util.RefCounted;

public abstract class Asset extends RefCounted {
    private AssetManager manager;
    private ResourceFile source;

    protected abstract void freeAsset();

    @Override
    protected final void free() {
        System.out.println("[Debug] Freeing " + getClass());
        freeAsset();
    }

    public ResourceFile getSourceFile() {
        return source;
    }

    void setMetadata(AssetManager manager, ResourceFile source) {
        if (this.manager != null) {
            throw new IllegalStateException("Asset metadata already set");
        }
        this.manager = manager;
        this.source = source;
    }
}
