package com.github.rmheuer.engine.core.asset;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class AssetManager {
    private final Map<Class<? extends Asset>, AssetSerializer<? extends Asset>> serializers;
    private final Map<Pair<Class<? extends Asset>, ResourceFile>, Asset> assetCache;

    public AssetManager() {
        serializers = new HashMap<>();
        assetCache = new HashMap<>();
    }

    public <T extends Asset> void registerSerializer(Class<T> assetType, AssetSerializer<T> serializer) {
        if (serializers.containsKey(assetType))
            throw new IllegalStateException("Asset serializer already registered for " + assetType);

        serializers.put(assetType, serializer);
    }

    private <T extends Asset> T loadAsset(Class<T> assetType, ResourceFile src) {
        AssetSerializer<? extends Asset> wildcardSerializer = serializers.get(assetType);

        // This will always be safe because the value will always have the same
        // type as the key in the serializers map
        @SuppressWarnings("unchecked")
        AssetSerializer<T> serializer = (AssetSerializer<T>) wildcardSerializer;

        T asset;
        try {
            asset = serializer.deserialize(src, this);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load asset " + src.getPath() + " of type " + assetType);
        }

        asset.setMetadata(this, assetType, src);

        return asset;
    }

    void unloadAsset(Asset asset) {
        Pair<Class<? extends Asset>, ResourceFile> pair = new Pair<>(asset.getType(), asset.getSourceFile());
        assetCache.remove(pair);
    }

    public <T extends Asset> T getAsset(Class<T> assetType, ResourceFile src) {
        Pair<Class<? extends Asset>, ResourceFile> pair = new Pair<>(assetType, src);
        Asset asset = assetCache.computeIfAbsent(pair, (p) -> loadAsset(p.getA(), p.getB()));

        // Asset cache will only ever contain assets that match the specified type
        @SuppressWarnings("unchecked")
        T t = (T) asset;

        return t;
    }
}
