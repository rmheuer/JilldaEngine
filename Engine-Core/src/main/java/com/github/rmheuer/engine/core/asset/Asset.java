package com.github.rmheuer.engine.core.asset;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.util.RefCounted;

import java.util.ArrayList;
import java.util.List;

public abstract class Asset extends RefCounted {
    // TODO: Maybe move to AssetManager
    private static final List<Asset> activeAssets = new ArrayList<>();
    public static void cleanUp() {
        System.out.println("Cleaning up assets");
        List<Asset> copy = new ArrayList<>(activeAssets);
        for (Asset asset : copy) {
            if (activeAssets.remove(asset)) {
                System.out.println("[Debug] Cleaning up " + asset.getClass());
                asset.freeAsset();
            }
        }
    }

    private AssetManager manager;
    private Class<? extends Asset> type;
    private ResourceFile source;

    public Asset() {
        activeAssets.add(this);
        System.out.println("[Debug] Allocating " + getClass());
    }

    protected abstract void freeAsset();

    @Override
    protected final void free() {
        if (!activeAssets.remove(this))
            return;

        if (manager != null)
            manager.unloadAsset(this);

        System.out.println("[Debug] Dynamic freeing " + getClass());
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
