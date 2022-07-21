package com.github.rmheuer.engine.core.asset;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.util.RefCounted;

import java.util.ArrayList;
import java.util.List;

public abstract class Asset extends RefCounted {
    // TODO: Maybe move to AssetManager
    private static final List<Asset> activeAssets = new ArrayList<>();
    public static void cleanUp() {
        for (Asset asset : activeAssets) {
            System.out.println("[Debug] Freeing " + asset.getClass());
            asset.freeAsset();
        }
    }

    private AssetManager manager;
    private Class<? extends Asset> type;
    private ResourceFile source;

    public Asset() {
        activeAssets.add(this);
    }

    protected abstract void freeAsset();

    @Override
    protected final void free() {
        System.out.println("[Debug] Freeing " + getClass());
        activeAssets.remove(this);
        manager.unloadAsset(this);
        freeAsset();
    }

    public ResourceFile getSourceFile() {
        return source;
    }

    Class<? extends Asset> getType() {
        return type;
    }

    void setMetadata(AssetManager manager, Class<? extends Asset> type, ResourceFile source) {
        if (this.manager != null) {
            throw new IllegalStateException("Asset metadata already set");
        }
        this.manager = manager;
        this.type = type;
        this.source = source;
    }
}
